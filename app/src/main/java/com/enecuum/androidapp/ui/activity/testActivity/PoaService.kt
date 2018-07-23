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
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*


class PoaService(val context: Context, val BN_PATH: String, val BN_PORT: String, val NN_PATH: String, val NN_PORT: String, val onTeamSize: onTeamListener) {

    val blockSize = 512 * 1024;
    val TEAM_WS_IP = "195.201.217.44"
    val TEAM_WS_PORT = "8080"

    val TRANSACTION_COUNT_IN_MICROBLOCK = 1

    var composite = CompositeDisposable()
    var websocket: WebSocket? = null;

    var gson: Gson = GsonBuilder().disableHtmlEscaping().create();
    private val websocketEvents: ConnectableFlowable<WebSocketEvent>;
    val webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>;
    var bootNodeWebsocket: ConnectableFlowable<WebSocketEvent>;

    init {
        Timber.d("Start testing")
        val s1 = "W3sidGltZSI6MTUzMjM3MTk0Miwibm9uY2UiOjE5NTQ2NiwibnVtYmVyIjoxOTksInR5cGUiOjAsInByZXZfaGFzaCI6IkFBQUJSd3daNkhXTWVOQ3NWdUtUZCtmK2p5dW41K0NrM3oxL1duR1V6S0k9Iiwic29sdmVyIjoiT3ZTOExtbWNNYTRtdEVXYmlmTzVaRmtxVDZBWVJpenpRNm1Fb2JNTWh6ND0ifV0="

        bootNodeWebsocket = getWebSocket(BN_PATH, BN_PORT).observe()
                .publish()

        composite.add(bootNodeWebsocket
                .filter { it is WebSocketEvent.OpenedEvent }
                .doOnNext({
                    Timber.d("Connected to BN, sending request")
                    it.webSocket?.send(gson.toJson(ConnectBNRequest()))
                })
                .subscribe())

        websocketEvents =
                bootNodeWebsocket
                        .filter { it is WebSocketEvent.StringMessageEvent }
                        .cast(WebSocketEvent.StringMessageEvent::class.java)
                        .map { parse(it.text!!) }
                        .cast(ConnectBNResponse::class.java)
                        .map {
                            Timber.d("Got NN nodes:" + it.toString())
                            val size = it.connects.size
                            val nextInt = Random().nextInt(size)
                            return@map it.connects.get(nextInt)
                        }
                        .flatMap {
                            Timber.d("Connecting to: ${it.ip}:${it.port}")
                            getWebSocket(it.ip, it.port).observe()
                        }
                        .doOnNext {
                            it.webSocket?.send(gson.toJson(ReconnectNotification()))
                            websocket = it.webSocket
                        }
                        .publish()

        webSocketStringMessageEvents = websocketEvents
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map {
                    Pair(it.webSocket, parse(it.text!!))
                }

        composite.add(websocketEvents.doOnNext({
            when (it) {
                is WebSocketEvent.StringMessageEvent -> Timber.i("Recieved message:" + it.text);
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

    private lateinit var team: List<String>

    private lateinit var myId: String

    fun connect() {


        val myId = webSocketStringMessageEvents
                .filter { it.second is ReconnectResponse }
                .doOnNext {
                    val teamWs = getWebSocket(TEAM_WS_IP, TEAM_WS_PORT)
                            .observe()
                            .share()
                    val myNodeId = (it.second as ReconnectResponse).node_id
                    val ws = it.first
                    Timber.d("My id: $myNodeId")
                    Timber.d("Joining to team")
//                    val nodeId = gson.toJson(PoANodeUUIDResponse(nodeId = myNodeId))
//                    ws?.send(nodeId)
                    myId = myNodeId
                    Timber.d("Sent NodeId reason")
                    teamWs
                            .doOnError { Timber.e(it.localizedMessage) }
                            .filter { it is WebSocketEvent.OpenedEvent }
                            .subscribe {
                                Timber.d("Sending my id: " + myNodeId)
                                it.webSocket?.send(gson.toJson(PoANodeUUIDResponse(nodeId = myNodeId)))
                            }

                    teamWs.doOnError { Timber.e(it.localizedMessage) }
                            .filter { it is WebSocketEvent.StringMessageEvent }
                            .cast(WebSocketEvent.StringMessageEvent::class.java)
                            .map { parse(it.text!!) }
                            .cast(TeamResponse::class.java)
                            .subscribe {
                                Timber.i("Team size updated")
                                val size = it.data.size
                                Timber.d("Command size: " + size)
                                onTeamSize.onTeamSize(size)
                                team = it.data
                                if (size > 1) {
                                    startWork(myNodeId, webSocketStringMessageEvents, ws)
                                }
                            }

                }

        composite.add(myId.subscribe())

        websocketEvents.connect()

        bootNodeWebsocket.connect()

    }


    fun startEvent() {
        gotKeyBlock(ReceivedBroadcastKeyblockMessage(keyBlock = Keyblock(body = "fdf", verb = "dfs")), websocket = websocket!!)
    }

    var currentTransactions: List<Transaction> = listOf();

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
                    gson.fromJson(addressedMessageResponse.msg, ResponseSignature::class.java);
                }
                .distinctUntilChanged()
                .buffer(team.size - 1)  //we need singns from all teams memeber except himself
                .doOnNext({
                    Timber.i("Signed all successfully")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Sending", Toast.LENGTH_LONG).show()
                    }

                    val keyblockBodyJson = decode64(keyblockResponse?.body!!)
                    val kBlockStructure = gson.fromJson(keyblockBodyJson, Array<KBlockStructure>::class.java)
                    val kBlockStructure1 = kBlockStructure.get(0)
                    val bb = ByteArrayOutputStream()
                    bb.write(intToLittleEndian(kBlockStructure1.time))
                    bb.write(intToLittleEndian(kBlockStructure1.nonce))
                    bb.write(intToLittleEndian(kBlockStructure1.number.toLong()))
                    bb.write(intToLittleEndian(kBlockStructure1.type.toLong()))
                    bb.write(kBlockStructure1.prev_hash.toByteArray())
                    bb.write(kBlockStructure1.solver.toByteArray())
                    val toByteArray = bb.toByteArray()
                    val hash256 = hash256(toByteArray)
                    val encode64 = encode64(hash256)

                    val microblockMsg = MicroblockMsg(Tx = currentTransactions,
                            K_hash = encode64,
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

                    currentTransactions = listOf()
                    askForNewTransactions(websocket);
                })
                .subscribe())

        composite.add(
                transactionResponses
                        .filter { it.second is TransactionResponse }
                        .map { it.second }
                        .doOnNext { Timber.d("Got transaction: ${it}") }
                        .cast(TransactionResponse::class.java)
                        .doOnNext({

                            currentTransactions += it.transactions

                            if (currentTransactions.size >= TRANSACTION_COUNT_IN_MICROBLOCK) {
                                Timber.i("START asking for sign")
                                if (team.size > 1) {
                                    for (teamMember in team) {
                                        if (teamMember == myId) {
                                            continue
                                        }
                                        val message = gson.toJson(RequestForSignature(data = currentTransactions.toString()))
                                        val toJson = gson.toJson(AddressedMessageRequest(msg = message, to = teamMember, from = myId))
                                        websocket?.send(toJson)
                                    }
                                } else {
                                    Timber.e("Team is empty")
                                }

                            }

                        }).subscribe()
        )


        composite.add(
                broadcastKeyBlockMessage
                        .doOnNext {
                            val response = it.second as ReceivedBroadcastKeyblockMessage;
                            gotKeyBlock(response, websocket)
                        }.subscribeOn(Schedulers.io()).subscribe())

        composite.add(
                addressedMessageResponse
                        .doOnComplete({ Timber.e("Complete!!!") })

                        .doOnNext {
                            val before = System.currentTimeMillis();
                            val response = it.second as AddressedMessageResponse;
                            if (response.from == myId) {
                                Timber.d("Message from me, skipping...")
                            }

                            val requestForSignature = gson.fromJson(response.msg, RequestForSignature::class.java)
                            //we have request For Signature
                            if (requestForSignature.data != null) {
                                Timber.d("Request for signature from: ${response.from} ")
                                val hash256 = hash256(requestForSignature.data!!);
                                Timber.d("Processing hash: ${System.currentTimeMillis() - before} millis ")
                                val enc = RSACipher().encrypt(hash256);
                                val period = System.currentTimeMillis() - before;

                                val responseSignature = ResponseSignature(signature = Signature(myId, hash256, enc))

                                Timber.d("Processing total time: $period millis ")
                                val addressedMessageRequest = AddressedMessageRequest(
                                        to = response.from,
                                        msg = gson.toJson(responseSignature),
                                        from = myId)
                                Timber.d("Signed message from: ${response.from} by ${myId} ")
                                it.first?.send(gson.toJson(addressedMessageRequest))
                            }

                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe());


    }

    private fun gotKeyBlock(response: ReceivedBroadcastKeyblockMessage, websocket: WebSocket?) {
        Timber.d("Got key block, start asking for transactions")
        keyblockResponse = response.keyBlock
        askForNewTransactions(websocket)
    }


    public fun askForNewTransactions(websocket: WebSocket?) {
        websocket?.send(gson.toJson(TransactionRequest()));
    }

    public fun askForNewTransactions() {
        websocket?.send(gson.toJson(TransactionRequest()));
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

    private fun encode64(src: String): String =
            BaseEncoding.base64().encode(String(src.toByteArray(Charsets.US_ASCII), Charsets.US_ASCII).toByteArray())

    private fun encode64(src: ByteArray): String =
            BaseEncoding.base64().encode(src)

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
        if (!JsonUtils.isJSONValid(text)) {
            Timber.e("String is not JSON: " + text)
            return Object()
        }

        val fromJson = gson.fromJson(text, BasePoAMessage::class.java);
        val type = fromJson.type
        return parse(type, text)
    }

    private fun parse(type: String, text: String?): Any? {
        Timber.d("Parsing: ${text}")
        val any = when (type) {
            CommunicationSubjects.Team.name -> gson.fromJson(text, TeamResponse::class.java)
            CommunicationSubjects.PotentialConnects.name -> gson.fromJson(text, ConnectBNResponse::class.java)
            CommunicationSubjects.Connect.name -> gson.fromJson(text, ReconnectNotification::class.java)
            CommunicationSubjects.Broadcast.name -> gson.fromJson(text, ReceivedBroadcastMessage::class.java)
            CommunicationSubjects.KeyBlock.name -> gson.fromJson(text, ReceivedBroadcastKeyblockMessage::class.java)
            CommunicationSubjects.MsgTo.name -> gson.fromJson(text, AddressedMessageResponse::class.java)
            CommunicationSubjects.PoWList.name -> gson.fromJson(text, PowsResponse::class.java)
            CommunicationSubjects.NodeId.name -> gson.fromJson(text, ReconnectResponse::class.java)
//        CommunicationSubjects.NodeId.name -> if (text!!.contains("Response")) gson.fromJson(text, ReconnectResponse::class.java) else gson.fromJson(text, PoANodeUUIDRequest::class.java);
            CommunicationSubjects.Transactions.name -> gson.fromJson(text, TransactionResponse::class.java);
//            PoACommunicationSubjects.Keyblock.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWTailResponse::class.java)
            PoACommunicationSubjects.Peek.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWPeekResponse::class.java)
            CommunicationSubjects.Error.name -> gson.fromJson(text, ErrorResponse::class.java)
            else -> {
                throw IllegalArgumentException("Can't parse type: $type with messages: $text")
            };
        }
        return any
    }

    public interface onTeamListener {
        fun onTeamSize(size: Int)
    }


    private fun intToLittleEndian(numero: Long): ByteArray {
        val bb = ByteBuffer.allocate(4)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.putInt(numero.toInt())
        return bb.array()
    }

}