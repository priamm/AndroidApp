package com.enecuum.androidapp.ui.activity.testActivity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.Toast
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.inherited.models.*
import com.enecuum.androidapp.models.inherited.models.Sha.hash256
import com.enecuum.androidapp.network.RxWebSocket
import com.enecuum.androidapp.network.WebSocketEvent
import com.google.gson.Gson
import com.jraska.console.timber.ConsoleTree
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.test_activity.*
import okhttp3.Request
import okhttp3.WebSocket
import timber.log.Timber
import java.nio.charset.Charset
import java.util.*


class TestActivity : AppCompatActivity() {

    val blockSize = 512 * 1024;
    private val BN_PATH = "195.201.226.28"
    private val BN_PORT = "1554"
    private val NN_PATH = "88.99.86.129"//"195.201.226.25"
    private val NN_PORT = "1554"

    val TRANSACTION_COUNT_FOR_REQUEST = 1

    val StartMsg = "{\"verb\":\"block\",\"body\":Img\"\"}";

    var testGson = "{\"node\":[\"5c300af5-641d-4981-ac24-69c9c33d76db\",\"0e00718d-d067-4c05-b897-4a23051862da\",\"b373b275-30e1-49ad-bbeb-6055943b3de7\",\"c045482b-8ea1-4f53-a2b4-8dc33dee6682\",\"9d964f2b-0417-4975-bd57-8e320d3e52eb\",\"4c6ee73b-43de-4adc-be41-2a07711efc82\",\"3ef45d69-1a5a-4f43-9a61-3e7116044c31\",\"4a22c9f1-f0b7-4e8c-bf41-ce4589e4a441\"]}"
    val poa_count = 2;
    val gson = Gson()
    val string1mb = create()
    var composite = CompositeDisposable()
    var webs: WebSocket? = null;
    var nodes: Nodes = gson.fromJson(testGson, Nodes::class.java);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)
        Timber.plant(ConsoleTree());
        //for generate purposes
//        val arrayList = ArrayList<String>()
//        for (i in 8 downTo 1) arrayList.add(UUID.randomUUID().toString())
//        val nodes1 = Nodes(arrayList)
//        val testGson = gson.toJson(nodes1);

        testGson = testGson.replace("-", "");
        jsonPath.setText(testGson)
        Timber.d("Start testing")

        val websocketEvents = getWebSocket(NN_PATH, NN_PORT).observe()
                .doOnNext { webs = it.webSocket }
                .publish()

        val webSocketStringMessageEvents = websocketEvents
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
                is WebSocketEvent.FailureEvent -> Timber.e("WS Failue Event :${it.t?.localizedMessage}, ${it.response.toString()}");
            }
        }).subscribe());

        start.setOnClickListener({
            nodes = gson.fromJson(jsonPath.text.toString(), Nodes::class.java)
            val index = Integer.valueOf(myNumber.text.toString())
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
                        Timber.d("Sent NodeId response")
                        startWork(myId, webSocketStringMessageEvents, it.first)
                    }

            composite.add(nodeIdRequest.subscribe())


            Timber.d("My id: $myId")

            websocketEvents.connect()
        })


        start_event.setOnClickListener({
            val keyblockResponse = gson.toJson(KeyblockResponse(keyblock = Keyblock("eHh4")))
            webs?.send(gson.toJson(BroadcastPoAMessage(msg = keyblockResponse)))
        })
    }

    var currentTransaction: String? = null;

    private var keyblockResponse: KeyblockResponse? = null

    private fun startWork(myId: String, webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>, websocket: WebSocket?) {
        val broadcastMessage = webSocketStringMessageEvents
                .filter { it.second is ReceivedBroadcastMessage }

        val addressedMessageResponse = webSocketStringMessageEvents
                .filter { it.second is AddressedMessageResponse }

        val transactionResponses = webSocketStringMessageEvents
                .filter({ it.second is TransactionResponse })

        composite.add(addressedMessageResponse
                .map {
                    val addressedMessageResponse = it.second as AddressedMessageResponse;
                    val decode64 = decode64(addressedMessageResponse.messages)
                    gson.fromJson(decode64, ResponseSignature::class.java);
                }
                .filter {
                    if (currentTransaction == null) false;
                    else
                        hash256(currentTransaction!!).equals(it.signature.hash)
                }
                .distinctUntilChanged()
                .buffer(poa_count)
                .doOnNext({
                    Timber.i("Signed all successfully")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this, "Sending", Toast.LENGTH_LONG).show()
                    }

                    val base64String = "SoMeBaSe64StRinG=="
                    val transaction = TransactionOut(from = base64String, to = base64String, amount = 1, uuid = myId)

                    val microblockMsg = MicroblockMsg(Tx = listOf(transaction),
                            K_hash = keyblockResponse?.keyblock?.body!!,
                            wallets = listOf(1, 2),
                            i = Random().nextInt())


                    val microblockResponse = MicroblockResponse(
                            microblock = Microblock(microblockMsg, sign = MicroblockSignature(34, 43)))

                    Timber.i("Sending to NN")
                    websocket?.send(gson.toJson(microblockResponse))
                })
                .subscribe())

        composite.add(
                transactionResponses
                        .filter { it.second is TransactionResponse }
                        .map { it.second }
                        .cast(TransactionResponse::class.java)
                        .map { it.transaction }
                        .buffer(TRANSACTION_COUNT_FOR_REQUEST)
                        .doOnNext({
                            var sb = StringBuilder()
                            for (s in it) {
                                sb.append(s)
                            }
                            currentTransaction = sb.toString()
                            Timber.i("START message")
                            val flowMessage = RequestForSignature(data = currentTransaction!!)
                            val message = encode64(gson.toJson(flowMessage))
                            val toJson = gson.toJson(BroadcastPoAMessage(msg = message))
                            websocket?.send(toJson)
                            Timber.d("Sending broadcast")
                        }).subscribe()
        )


        composite.add(
                broadcastMessage
                        .doOnComplete({ Timber.e("Complete!!!") })
                        .doOnNext {
                            val before = System.currentTimeMillis();
                            val response = it.second as ReceivedBroadcastMessage;
                            if (response.messages.contains("eHh4")) {
                                keyblockResponse = gson.fromJson(response.messages, KeyblockResponse::class.java)
                                askForNewTransactions(websocket)
                                return@doOnNext
                            }
                            val string = decode64(response.messages)

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
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setTitle("Done")
                .setMessage("All signed")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    dialog.dismiss()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    private fun encode64(toJson: String) =
            Base64.encodeToString(toJson.toByteArray(Charset.forName("UTF-8")), Base64.DEFAULT)

    private fun decode64(messages1: String): String {
        val messages = Base64.decode(messages1, Base64.DEFAULT)
        val string = String(messages, Charset.forName("UTF-8"))
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
        val any = when (type) {
            CommunicationSubjects.Connects.name -> gson.fromJson(text, ConnectResponse::class.java)
            CommunicationSubjects.Connect.name -> gson.fromJson(text, ReconnectNotification::class.java)
            CommunicationSubjects.BroadcastMsg.name -> gson.fromJson(text, ReceivedBroadcastMessage::class.java)
            CommunicationSubjects.MsgTo.name -> gson.fromJson(text, AddressedMessageResponse::class.java)
            CommunicationSubjects.PoWList.name -> gson.fromJson(text, PowsResponse::class.java)
            CommunicationSubjects.NodeId.name -> if (text!!.contains("Response")) gson.fromJson(text, PoANodeUUIDResponse::class.java) else gson.fromJson(text, PoANodeUUIDRequest::class.java);
            CommunicationSubjects.Transaction.name -> gson.fromJson(text, TransactionResponse::class.java);
            PoACommunicationSubjects.Keyblock.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWTailResponse::class.java)
            PoACommunicationSubjects.Peek.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWPeekResponse::class.java)
            else -> {
                throw IllegalArgumentException("Can't parse type: $type with messages: $text")
            };
        }
        return any
    }
}