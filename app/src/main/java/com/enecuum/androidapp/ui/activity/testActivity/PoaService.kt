package com.enecuum.androidapp.ui.activity.testActivity

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.enecuum.androidapp.models.inherited.models.*
import com.enecuum.androidapp.models.inherited.models.Sha.hash256
import com.enecuum.androidapp.network.RxWebSocket
import com.enecuum.androidapp.network.WebSocketEvent
import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import okhttp3.WebSocket
import timber.log.Timber
import java.math.BigInteger
import java.util.*


class PoaService(val context: Context) {

    val blockSize = 512 * 1024;
    private val BN_PATH = "195.201.226.28"//"88.99.86.200"
    private val BN_PORT = "1554"
    private val NN_PATH = "95.216.150.210"//"195.201.226.30"//"195.201.226.25"
    private val NN_PORT = "1554"

    val TRANSACTION_COUNT_FOR_REQUEST = 1

    var testGson = "{\"node\":[\"5c300af5-641d-4981-ac24-69c9c33d76db\",\"0e00718d-d067-4c05-b897-4a23051862da\",\"b373b275-30e1-49ad-bbeb-6055943b3de7\",\"c045482b-8ea1-4f53-a2b4-8dc33dee6682\",\"9d964f2b-0417-4975-bd57-8e320d3e52eb\",\"4c6ee73b-43de-4adc-be41-2a07711efc82\",\"3ef45d69-1a5a-4f43-9a61-3e7116044c31\",\"4a22c9f1-f0b7-4e8c-bf41-ce4589e4a441\"]}"
    val poa_count = 2;

    var composite = CompositeDisposable()
    var websocket: WebSocket? = null;

    val string1mb = create()
    var gson: Gson = GsonBuilder().disableHtmlEscaping().create();
    lateinit var nodes: Nodes
    private val websocketEvents: ConnectableFlowable<WebSocketEvent>;
    val webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>;
    var bootNodeWebsocket: ConnectableFlowable<WebSocketEvent>;

    init {

        //for generate purposes
//        val arrayList = ArrayList<String>()
//        for (i in 8 downTo 1) arrayList.add(UUID.randomUUID().toString())
//        val nodes1 = Nodes(arrayList)
//        val testGson = gson.toJson(nodes1);

        nodes = gson.fromJson(testGson, Nodes::class.java);
        testGson = testGson.replace("-", "");
        Timber.d("Start testing")

        bootNodeWebsocket = getWebSocket(BN_PATH, BN_PORT).observe()
                .publish()

        composite.add(bootNodeWebsocket
                .filter { it is WebSocketEvent.OpenedEvent }
                .doOnNext({
                    Timber.d("Connected to BN, sending request")
                    it.webSocket?.send(gson.toJson(ConnectRequest()))
                }).subscribe())

        websocketEvents =
                bootNodeWebsocket
                        .filter { it is WebSocketEvent.StringMessageEvent }
                        .cast(WebSocketEvent.StringMessageEvent::class.java)
                        .map { parse(it.text!!) }
                        .cast(ConnectResponse::class.java)
                        .map {
                            Timber.d("Got NN nodes:" + it.toString())
                            val size = it.connects.size
                            if (size == 0) {
                                Timber.e("There is no NN")
                                return@map ConnectPointDescription(BN_PATH, BN_PORT);
                            }
                            val nextInt = Random().nextInt(size)
                            return@map it.connects.get(nextInt)
                        }
                        .flatMap {
                            //                            it.port
                            getWebSocket(it.ip, "1554").observe()
                        }
                        .doOnNext { websocket = it.webSocket }
                        .publish()

        webSocketStringMessageEvents = websocketEvents
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map {
                    Pair(it.webSocket, parse(it.text!!))
                }

        composite.add(websocketEvents.doOnNext({
            when (it) {
                is WebSocketEvent.StringMessageEvent -> Timber.i("Recieved message");
                is WebSocketEvent.OpenedEvent -> Timber.i("WS Opened Event");
                is WebSocketEvent.ClosedEvent -> Timber.i("WS Closed Event");
                is WebSocketEvent.FailureEvent -> {
                    val s = "WS Failue Event :${it.t?.localizedMessage}, ${it.response.toString()}"
                    Timber.e(s)
                    Handler(Looper.getMainLooper()).post { Toast.makeText(context, s, Toast.LENGTH_SHORT).show() }
                };
            }
        }).subscribe());


    }

    fun connectAs(index: Int) {
        nodes = gson.fromJson(testGson, Nodes::class.java)
        if (index > poa_count) {
            throw IllegalArgumentException("id number should be below or equal poa count")
        }
        val myId = nodes.node.get(index - 1)

        val nodeIdRequest = webSocketStringMessageEvents
                .filter { it.second is PoANodeUUIDRequest }
                .doOnNext {
                    Timber.d("Got NodeId request")
                    val nodeId = gson.toJson(PoANodeUUIDResponse(nodeId = myId))
                    it.first?.send(nodeId)
                    Timber.d("Sent NodeId reason")
                    startWork(myId, webSocketStringMessageEvents, it.first)
                }

        composite.add(nodeIdRequest.subscribe())


        Timber.d("My id: $myId")

        websocketEvents.connect()

        bootNodeWebsocket.connect()

    }

    //    {"tag":"Msg","idFrom":"33333333333333333333333333333333","msg":{"body":"W3sidGltZSI6MTUyOTU5NDA5OSwibm9uY2UiOjI0NTE1MSwibnVtYmVyIjozLCJ0eXBlIjowLCJwcmV2X2hhc2giOiJBQUFBQVJ3RzRJVEtuR2lNNEx5VVNvSHhBYnpNWGNiMmdqVmxOZVJSQjJrPSJ9XQ==","verb":"block"},"type":"Broadcast"}
    fun startEvent() {
//        {"body":"W3sidGltZSI6MTUyOTU5NDA5OSwibm9uY2UiOjI0NTE1MSwibnVtYmVyIjozLCJ0eXBlIjowLCJwcmV2X2hhc2giOiJBQUFBQVJ3RzRJVEtuR2lNNEx5VVNvSHhBYnpNWGNiMmdqVmxOZVJSQjJrPSJ9XQ==","verb":"block"}
//        val keyblockResponse = gson.toJson(keyblock = Keyblock("eHh4"))
//        websocket?.send(gson.toJson(BroadcastPoAMessage(msg = keyblockResponse)))
        askForNewTransactions(websocket)
    }

    var currentTransactions: List<Transaction>? = null;

    private var keyblockResponse: Keyblock? = null

    private fun startWork(myId: String, webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>, websocket: WebSocket?) {
        val broadcastMessage = webSocketStringMessageEvents
                .filter { it.second is ReceivedBroadcastMessage }

        val broadcastKeyBlockMessage = webSocketStringMessageEvents
                .filter { it.second is ReceivedBroadcastKeyblockMessage }

        val addressedMessageResponse = webSocketStringMessageEvents
                .filter { it.second is AddressedMessageResponse }

        val transactionResponses = webSocketStringMessageEvents
                .filter({ it.second is TransactionResponse })

        val errorMessageResponse = webSocketStringMessageEvents
                .filter { it.second is ErrorResponse }

        composite.add(
                errorMessageResponse
                        .doOnNext {
                            val errorResponse = it.second as ErrorResponse
                            Timber.e("Error: ${errorResponse.reason}")
                            Timber.e("Error: ${errorResponse.Msg}")
                        }
                        .subscribe()
        )

        composite.add(addressedMessageResponse
                .map {
                    val addressedMessageResponse = it.second as AddressedMessageResponse;
                    val decode64 = decode64(addressedMessageResponse.msg)
                    gson.fromJson(decode64, ResponseSignature::class.java);
                }
                .filter {
                    if (currentTransactions == null) false;
                    else
                        hash256(currentTransactions!!.toString()).equals(it.signature.hash)
                }
                .distinctUntilChanged()
                .buffer(poa_count)
                .doOnNext({
                    Timber.i("Signed all successfully")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Sending", Toast.LENGTH_LONG).show()
                    }

                    val base64String = "SoMeBaSe64StRinG=="

                    val microblockMsg = MicroblockMsg(Tx = currentTransactions!!,
                            K_hash = keyblockResponse?.body ?: "eHh4",
                            wallets = listOf("1", "2")
                    )


                    val sign_r = BigInteger.TEN;
                    val sign_s = BigInteger.TEN;
                    val microblockResponse = MicroblockResponse(
                            microblock = Microblock(microblockMsg, sign =
                            MicroblockSignature(
                                    sign_r = "NDU=",//encode64(sign_r.toByteArray()),
                                    sign_s = "NDU=")))//encode64(sign_s.toByteArray()))))
                    Timber.i("Sending to NN")
                    websocket?.send(gson.toJson(microblockResponse))
                })
                .subscribe())

        composite.add(
                transactionResponses
                        .filter { it.second is TransactionResponse }
                        .map { it.second }
                        .doOnNext { Timber.d("Got transaction: ${it}") }
                        .cast(TransactionResponse::class.java)
                        .map { it.transaction }
                        .buffer(TRANSACTION_COUNT_FOR_REQUEST)
                        .doOnNext({
                            var sb = StringBuilder()
                            for (s in it) {
                                sb.append(s)
                            }
                            currentTransactions = it
                            Timber.i("START message")
                            val requestForSignature = RequestForSignature(data = currentTransactions!!.toString())
                            val message = encode64(gson.toJson(requestForSignature))
                            val toJson = gson.toJson(BroadcastPoAMessage(msg = message))
                            websocket?.send(toJson)
                            Timber.d("Sending broadcast")
                        }).subscribe()
        )


        composite.add(
                broadcastKeyBlockMessage
                        .doOnNext {
                            val response = it.second as ReceivedBroadcastKeyblockMessage;
                            Timber.d("Got key block, start asking for transactions")
                            keyblockResponse = response.msg
                            askForNewTransactions(websocket)
                        }.subscribeOn(Schedulers.io()).subscribe())

        composite.add(
                broadcastMessage
                        .doOnComplete({ Timber.e("Complete!!!") })
                        .doOnNext {
                            val before = System.currentTimeMillis();
                            val response = it.second as ReceivedBroadcastMessage;
                            if (response.idFrom == myId) {
                                Timber.d("Message from me, skipping...")
                            }
                            val string = decode64(response.msg)

                            val requestForSignature = gson.fromJson(string, RequestForSignature::class.java);
                            Timber.d("Request for signature from: ${response.idFrom} ")

                            val hash256 = hash256(requestForSignature.data);
                            Timber.d("Processing hash: ${System.currentTimeMillis() - before} millis ")
                            val enc = RSACipher().encrypt(hash256);
                            val period = System.currentTimeMillis() - before;

                            val responseSignature = ResponseSignature(Signature(myId, hash256, enc))

                            Timber.d("Processing total time: $period millis ")
                            val toJson = gson.toJson(responseSignature)
                            val message = encode64(toJson)
                            val addressedMessageRequest = AddressedMessageRequest(destination = response.idFrom, msg = message)
                            Timber.d("Signed message from: ${response.idFrom} by ${myId} ")
                            it.first?.send(gson.toJson(addressedMessageRequest))
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe());


    }

    private fun askForNewTransactions(websocket: WebSocket?) {
        websocket?.send(gson.toJson(TransactionRequest(number = TRANSACTION_COUNT_FOR_REQUEST)));
    }

    private fun showDoneDialog() {
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(context)
        }
        builder.setTitle("Done")
                .setMessage("All signed")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    dialog.dismiss()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    private fun encode64(src: String) =
            BaseEncoding.base64().encode(String(src.toByteArray(Charsets.US_ASCII), Charsets.US_ASCII).toByteArray())

    private fun decode64(messages1: String): String {
        val messages = BaseEncoding.base64().decode(messages1)
        val string = String(messages, Charsets.US_ASCII)
        return string
    }

    private fun create(): String {
        val chars = CharArray(blockSize)
        Arrays.fill(chars, 'f')
        return String(chars)
    }

    private fun getWebSocket(ip: String,
                             port: String): RxWebSocket {
        val request = Request.Builder().url("ws://$ip:$port").build()
        val webSocket = RxWebSocket.createAutoManagedRxWebSocket(request)
        return webSocket
    }

    private fun parse(text: String): Any? {

        val fromJson = gson.fromJson(text, BasePoAMessage::class.java);
        val type = fromJson.type
        return parse(type, text)
    }

    private fun parse(type: String, text: String?): Any? {
//        Timber.d("Parsing: ${text}")
        val any = when (type) {
            CommunicationSubjects.Connects.name -> gson.fromJson(text, ConnectResponse::class.java)
            CommunicationSubjects.Connect.name -> gson.fromJson(text, ReconnectNotification::class.java)
            CommunicationSubjects.Broadcast.name -> {
                if (text!!.contains("\"verb\":\"block\"")) {
                    gson.fromJson(text, ReceivedBroadcastKeyblockMessage::class.java)
                } else
                    gson.fromJson(text, ReceivedBroadcastMessage::class.java)
            }
            CommunicationSubjects.MsgTo.name -> {
                gson.fromJson(text, AddressedMessageResponse::class.java)
            }
            CommunicationSubjects.PoWList.name -> gson.fromJson(text, PowsResponse::class.java)
            CommunicationSubjects.NodeId.name -> if (text!!.contains("Response")) gson.fromJson(text, PoANodeUUIDResponse::class.java) else gson.fromJson(text, PoANodeUUIDRequest::class.java);
            CommunicationSubjects.Transaction.name -> gson.fromJson(text, TransactionResponse::class.java);
            PoACommunicationSubjects.Keyblock.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWTailResponse::class.java)
            PoACommunicationSubjects.Peek.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWPeekResponse::class.java)
            CommunicationSubjects.Error.name -> gson.fromJson(text, ErrorResponse::class.java)
            else -> {
                throw IllegalArgumentException("Can't parse type: $type with messages: $text")
            };
        }
        return any
    }
}