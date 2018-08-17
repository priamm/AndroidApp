package com.enecuum.androidapp.ui.activity.testActivity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.Toast
import com.enecuum.androidapp.models.inherited.models.*
import com.enecuum.androidapp.models.inherited.models.Sha.hash256
import com.enecuum.androidapp.network.RxWebSocket
import com.enecuum.androidapp.network.WebSocketEvent
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.google.common.io.BaseEncoding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import okhttp3.WebSocket
import timber.log.Timber
import java.io.EOFException
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.text.DateFormat
import java.util.*


class PoaService(val context: Context,
                 val BN_PATH: String,
                 val BN_PORT: String,
                 val nnFirtConnection: ConnectPointDescription?,
                 val onTeamSize: onTeamListener,
                 val onMicroblockCountListerer: onMicroblockCountListener,
                 val onConnectedListener1: onConnectedListener) {

    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    val blockSize = 512 * 1024;
    val TEAM_WS_IP = "195.201.217.44"
    val TEAM_WS_PORT = "8080"

    val TRANSACTION_COUNT_IN_MICROBLOCK = 1

    var composite: CompositeDisposable = CompositeDisposable()
    var websocket: WebSocket? = null;

    var gson: Gson = GsonBuilder().disableHtmlEscaping().create();
    val webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>;
    var bootNodeWebsocket: ConnectableFlowable<WebSocketEvent>;

    val websocketEvents: ConnectableFlowable<WebSocketEvent>
    val rsaCipher = RSACipher()

    var currentNodes: List<ConnectPointDescription>? = listOf()

    init {
        Timber.d("Start testing")

        if (PersistentStorage.getAddress().isEmpty()) {
            val random = SecureRandom()
            val bytes = ByteArray(32)
            random.nextBytes(bytes)
            PersistentStorage.setAddress(Base58.encode(bytes))
        }
        bootNodeWebsocket = getWebSocket(BN_PATH, BN_PORT).observe()
                .observeOn(AndroidSchedulers.mainThread())
                .publish()

        composite.add(bootNodeWebsocket
                .filter { it is WebSocketEvent.OpenedEvent }
                .doOnNext({
                    Timber.d("Connected to BN, sending request")
                    it.webSocket?.send(gson.toJson(ConnectBNRequest()))
                })
                .subscribe())

        composite.add(
                bootNodeWebsocket
                        .filter { it is WebSocketEvent.FailureEvent }
                        .map { it as WebSocketEvent.FailureEvent }
                        .doOnNext({
                            val isEof = it.t is EOFException
                            if (!isEof) {
                                reconnectAll(null)
                            }
                        })
                        .subscribe())

        websocketEvents = bootNodeWebsocket
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map { parse(it.text!!) }
                .filter({ it is ConnectBNResponse })
                .cast(ConnectBNResponse::class.java)
                .map {
                    Timber.d("Got NN nodes:" + it.toString())
                    currentNodes = it.connects;
                    if (nnFirtConnection != null) {
                        return@map nnFirtConnection
                    }
                    val size = it.connects.size
                    if (size > 0) {
                        return@map it.connects.get(0)
                    } else {
                        //TODO decide what to do if response empty
                        return@map ConnectPointDescription(BN_PATH, BN_PORT)
                    }
                }
                .flatMap {
                    val connectPointDescription = it
                    Timber.d("Connecting to: ${connectPointDescription.ip}:${connectPointDescription.port}")
                    reconnectToNN(connectPointDescription)
                }

                .publish()

        webSocketStringMessageEvents = websocketEvents
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map {
                    Pair(it.webSocket, parse(it.text!!))
                }

        composite.add(
                websocketEvents
                        .doOnNext({
                            when (it) {
                                is WebSocketEvent.StringMessageEvent -> Timber.i("Recieved message at:" + DateFormat.getDateTimeInstance().format(Date()))
                                is WebSocketEvent.OpenedEvent -> Timber.i("WS Opened Event");
                                is WebSocketEvent.ClosedEvent -> Timber.i("WS Closed Event");
                                is WebSocketEvent.FailureEvent -> {
                                    Timber.e("WS Failue Event :${it.t?.localizedMessage}, ${it.response.toString()}")
                                    if (currentNN != null && currentNodes != null) {
                                        val indexOf = currentNodes?.indexOf(currentNN!!)
                                        if (indexOf != -1 && (indexOf!! + 1) < currentNodes!!.size) {
                                            val reconnectTo = currentNodes?.get(indexOf + 1)
                                            reconnectAll(reconnectTo)
                                        } else {
                                            reconnectAll(null)
                                        }
                                    } else {
                                        reconnectAll(null)
                                    }
                                };
                            }
                        }).subscribe());


    }

    private var currentNN: ConnectPointDescription? = null

    private fun reconnectToNN(connectPointDescription: ConnectPointDescription): Flowable<WebSocketEvent>? {
        return getWebSocket(connectPointDescription.ip, connectPointDescription.port).observe()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it is WebSocketEvent.OpenedEvent) {
                        currentNN = connectPointDescription;
                        onConnectedListener1.onConnected(connectPointDescription.ip, connectPointDescription.port)

                        it.webSocket?.send(gson.toJson(ReconnectRequest()))
                        websocket = it.webSocket
                    }
                }
    }

    private fun reconnectAll(firstForReconnect: ConnectPointDescription?) {
        Timber.e("Will be reconnected in 5 ...")
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val intent = Intent("reconnectAll")
            intent.putExtra("reconnectNN", firstForReconnect)
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(intent);
        }, 5000)
    }

    private lateinit var team: List<String>

    private lateinit var myId: String

    fun disconnect() {
        websocket?.close(1000, "Client close");
        composite.clear()
    }

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
                    myId = myNodeId
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

    var currentTransactions: List<Transaction> = listOf();

    private var keyblockHash: String? = null

    private lateinit var prev_hash: String

    private var microblocksSoFar = 0

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
                            Timber.e("Error: ${errorResponse.comment}")
                            Timber.e("Error: ${errorResponse.Msg}")
                        }
                        .subscribe()
        )

        val publisher = Base58.encode(PersistentStorage.getAddress().toByteArray())
        composite.add(addressedMessageResponse
                .map {
                    val addressedMessageResponse = it.second as AddressedMessageResponse;
                    gson.fromJson(addressedMessageResponse.msg, ResponseSignature::class.java);
                }
                .filter {
                    //check if it is ResponseSignature actually
                    it.signature != null
                }
                .distinctUntilChanged()
                .buffer(team.size - 1)  //we need singns from all teams memeber except himself
                .doOnNext({
                    Timber.i("Signed all successfully")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Sending", Toast.LENGTH_LONG).show()
                    }
                    val publicKeys = mutableListOf<String>()
                    for (responseSignature in it) {
                        if (!TextUtils.isEmpty(responseSignature.signature?.publicKeyEncoded58)) {
                            responseSignature.signature?.publicKeyEncoded58?.let { it1 -> publicKeys.add(it1) }
                        }
                    }

                    val k_hash = keyblockHash
                    val microblockMsg = MicroblockMsg(Tx = currentTransactions,
                            publisher = publisher,
                            K_hash = k_hash!!,
                            wallets = publicKeys
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

                    onMicroblockCountListerer.onMicroblockCountAndLast(++microblocksSoFar, microblockResponse)
                    currentTransactions = listOf()
                    askForNewTransactions(websocket);
                })
                .subscribeOn(Schedulers.io())
                .subscribe())

        composite.add(
                transactionResponses
                        .filter { it.second is TransactionResponse }
                        .map({ it.second as TransactionResponse })
                        .doOnNext { Timber.d("Got : ${it.transactions.size} transactions") }
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
                        .doOnError({ Timber.e(it) })
                        .doOnNext {
                            val response = it.second as ReceivedBroadcastKeyblockMessage;
                            gotKeyBlock(response, websocket)
                        }.subscribe())

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
                                val enc = rsaCipher.encrypt(hash256);
                                val myEncodedPublicKey = Base58.encode(PersistentStorage.getAddress().toByteArray());
                                val period = System.currentTimeMillis() - before;

                                val responseSignature = ResponseSignature(signature = Signature(myId, hash256, enc, myEncodedPublicKey))

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

    private fun getKeyBlockHash(kBlockStructure: KBlockStructure): String {
        val toByteArray = toByteArray(kBlockStructure)
        val hash256 = hash256(toByteArray)
        return encode64(hash256)
    }


    private fun gotKeyBlock(response: ReceivedBroadcastKeyblockMessage, websocket: WebSocket?) {
        Timber.d("Got key block, start asking for transactions")

        val keyBlock = response.keyBlock;
        val keyblockBodyJson = decode64(keyBlock.body).toString(Charsets.US_ASCII)
        val kBlockStructure = gson.fromJson(keyblockBodyJson, Array<KBlockStructure>::class.java)
        keyblockHash = getKeyBlockHash(kBlockStructure.get(0))

        prev_hash = keyblockHash!!
        askForNewTransactions(websocket)
    }


    public fun askForNewTransactions(websocket: WebSocket?) {
        websocket?.send(gson.toJson(TransactionRequest(number = PersistentStorage.getCountTransactionForRequest())));
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
            BaseEncoding.base64().encode(src.toByteArray())

    private fun encode64(src: ByteArray): String =
            BaseEncoding.base64().encode(src)

    private fun decode64(messages1: String): ByteArray {
        return BaseEncoding.base64().decode(messages1)
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
//        Timber.d("Parsing: ${text}")
        val any = when (type) {
            CommunicationSubjects.Team.name -> gson.fromJson(text, TeamResponse::class.java)
            CommunicationSubjects.PotentialConnects.name -> gson.fromJson(text, ConnectBNResponse::class.java)
            CommunicationSubjects.Connect.name -> gson.fromJson(text, ReconnectRequest::class.java)
            CommunicationSubjects.Broadcast.name -> gson.fromJson(text, ReceivedBroadcastMessage::class.java)
            CommunicationSubjects.KeyBlock.name -> gson.fromJson(text, ReceivedBroadcastKeyblockMessage::class.java)
            CommunicationSubjects.MsgTo.name -> gson.fromJson(text, AddressedMessageResponse::class.java)
            CommunicationSubjects.PoWList.name -> gson.fromJson(text, PowsResponse::class.java)
            CommunicationSubjects.NodeId.name -> gson.fromJson(text, ReconnectResponse::class.java)
//        CommunicationSubjects.NodeId.name -> if (text!!.contains("Response")) gson.fromJson(text, ReconnectResponse::class.java) else gson.fromJson(text, PoANodeUUIDRequest::class.java);
            CommunicationSubjects.Transactions.name -> gson.fromJson(text, TransactionResponse::class.java);
//            PoACommunicationSubjects.Keyblock.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWTailResponse::class.java)
            PoACommunicationSubjects.Peek.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWPeekResponse::class.java)
            CommunicationSubjects.ErrorOfConnect.name -> gson.fromJson(text, ErrorResponse::class.java)
            CommunicationSubjects.Microblock.name -> gson.fromJson(text, MicroblockResponse::class.java)
            else -> {
                throw IllegalArgumentException("Can't parse type: $type with messages: $text")
            };
        }
        return any
    }

    public interface onTeamListener {
        fun onTeamSize(size: Int)
    }

    public interface onMicroblockCountListener {
        fun onMicroblockCountAndLast(count: Int, microblockResponse: MicroblockResponse)
    }

    public interface onConnectedListener {
        fun onConnected(ip: String, port: String);
    }

    private fun intToLittleEndian(numero: Long): ByteArray {

        val bb = ByteBuffer.allocate(4)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.putInt(numero.toInt())
        return bb.array()
    }

    private fun toByteArray(numero: KBlockStructure): ByteArray {
        val bb = ByteBuffer.allocate(256);
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.put(numero.type)
        bb.putInt(numero.number)
        bb.putInt(numero.time)
        bb.putInt(numero.nonce)
        bb.put(decode64(numero.prev_hash))
        bb.put(decode64(numero.solver))
        val position = bb.position()
        val out = mutableListOf<Byte>()
        for (index: Int in 0..(position - 1)) {
            out.add(bb.get(index))
        }
        return out.toByteArray()
    }

    fun getArray(buffer: ByteBuffer): ByteArray {
        val length = buffer.remaining()
        if (buffer.hasArray()) {
            val boff = buffer.arrayOffset() + buffer.position()
            return Arrays.copyOfRange(buffer.array(), boff, boff + length)
        }
        // else, DirectByteBuffer.get() is the fastest route
        val bytes = ByteArray(length)
        buffer.duplicate().get(bytes)

        return bytes
    }


    fun ByteArray.toHex(): String {
        val result = StringBuffer()

        forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }

        return result.toString()
    }


}