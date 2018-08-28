package com.enecuum.androidapp.ui.activity.testActivity

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Base64
import android.widget.Toast
import com.enecuum.androidapp.models.inherited.models.*
import com.enecuum.androidapp.models.inherited.models.Sha.hash256
import com.enecuum.androidapp.network.RxWebSocket
import com.enecuum.androidapp.network.WebSocketEvent
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.utils.ByteBufferUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import okhttp3.WebSocket
import timber.log.Timber
import java.math.BigInteger
import java.security.SecureRandom
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class PoaService(val context: Context,
                 val BN_PATH: String,
                 val BN_PORT: String,
                 val onTeamSize: onTeamListener,
                 val onMicroblockCountListerer: onMicroblockCountListener,
                 val onConnectedListner: onConnectedListener,
                 val balanceListener: BalanceListener) {

    val TEAM_WS_IP = "195.201.217.44"
    val TEAM_WS_PORT = "8080"

    val TRANSACTION_COUNT_IN_MICROBLOCK = 1

    private val PERIOD_ASK_FOR_BALANCE: Long = 60000

    var composite: CompositeDisposable = CompositeDisposable()
    var nnWs: WebSocket? = null;
    var teamWs: WebSocket? = null;
    var bootNodeWebSocket: WebSocket? = null

    var gson: Gson = GsonBuilder().disableHtmlEscaping().create()
    private lateinit var webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>
    private lateinit var bootNodeWebsocketEvents: Flowable<WebSocketEvent>
    private lateinit var nnWsEvents: Flowable<WebSocketEvent>
    val rsaCipher = RSACipher()
    var currentNodes: List<ConnectPointDescription>? = listOf()

    private var isConnectedVal: Boolean = false

    private var currentNN: ConnectPointDescription? = null

    var currentTransactions: List<Transaction> = listOf();

    private var keyblockHash: String? = null

    private lateinit var prev_hash: String

    private var microblocksSoFar = 0

    private val TRANSACTIONS_LIMIT_TO_PREVENT_OVERFLOW: Int = 1000

    private val MIN_K_BLOCK_PERIOD: Long = 10


    private fun reconnectToNN(connectPointDescription: ConnectPointDescription): Flowable<WebSocketEvent>? {
        return getWebSocket(connectPointDescription.ip, connectPointDescription.port)
                .doOnNext {
                    if (it is WebSocketEvent.OpenedEvent) {
                        currentNN = connectPointDescription;
                        isConnectedVal = true
                        onConnectedListner.onConnected(connectPointDescription.ip, connectPointDescription.port)
                        it.webSocket?.send(gson.toJson(ReconnectRequest()))
                        nnWs?.close(1000,"Close");
                        nnWs = it.webSocket
//                        startAskingForBalance(connectPointDescription.ip, connectPointDescription.port)
                    }
                }
                .subscribeOn(Schedulers.io())
    }

    private lateinit var team: List<String>

    private lateinit var myId: String

    fun disconnect() {
        isConnectedVal = false
        listOf(nnWs, bootNodeWebSocket, teamWs)
                .forEach {
                    it?.close(1000, "Client close");
                }
        composite.dispose()
        onConnectedListner.onDisconnected()
    }


    fun isConnected(): Boolean {
        return isConnectedVal;
    }

    fun connect() {
        Timber.d("Connecting ...")
        composite = CompositeDisposable()
        onConnectedListner.onDisconnected()

        createKey()

        bootNodeWebsocketEvents = getWebSocket(BN_PATH, BN_PORT);

        composite.add(bootNodeWebsocketEvents
                .filter { it is WebSocketEvent.OpenedEvent }
                .doOnNext {
                    bootNodeWebSocket?.close(1000,"Close");
                    bootNodeWebSocket = it.webSocket
                    Timber.d("Connected to BN, sending request")
                    it.webSocket?.send(gson.toJson(ConnectBNRequest()))
                }
                .subscribe())

        nnWsEvents = bootNodeWebsocketEvents
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map { parse(it.text!!) }
                .filter({ it is ConnectBNResponse })
                .cast(ConnectBNResponse::class.java)
                .doOnNext {
                    Timber.d("Got NN nodes:" + it.toString())
                    currentNodes = it.connects;
                }
                .flatMap { Flowable.fromIterable(it.connects) }
                .firstOrError()
                .doOnSuccess {
                    Timber.d("Connecting to: ${it.ip}:${it.port}")
                }.toFlowable()
                .switchMap {
                    reconnectToNN(it)
                }
                .cache()
                .subscribeOn(Schedulers.io());

        webSocketStringMessageEvents = nnWsEvents
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map {
                    Pair(it.webSocket, parse(it.text!!))
                }
                .cache()
                .subscribeOn(Schedulers.io())

        composite.add(
                nnWsEvents
                        .doOnNext {
                            when (it) {
                                is WebSocketEvent.StringMessageEvent -> Timber.i("Recieved message at:" + DateFormat.getDateTimeInstance().format(Date()))
                                is WebSocketEvent.OpenedEvent -> Timber.i("WS Opened Event");
                                is WebSocketEvent.ClosedEvent -> Timber.i("WS Closed Event");
                                is WebSocketEvent.FailureEvent -> {
                                    Timber.e("WS Failue Event :${it.t?.localizedMessage}, ${it.response.toString()}")
                                };
                            }
                        }.subscribe());

        val myId = webSocketStringMessageEvents
                .filter { it.second is ReconnectResponse }
                .doOnNext {
                    val teamWsEvents = getWebSocket(TEAM_WS_IP, TEAM_WS_PORT)
                    val myNodeId = (it.second as ReconnectResponse).node_id
                    val ws = it.first
                    Timber.d("My id: $myNodeId")
                    Timber.d("Joining to team")
                    myId = myNodeId
                    composite.add(
                            teamWsEvents
                                    .doOnError { Timber.e(it.localizedMessage) }
                                    .filter { it is WebSocketEvent.OpenedEvent }
                                    .subscribe {
                                        teamWs?.close(1000,"Close")
                                        teamWs = it.webSocket
                                        Timber.d("Sending my id: " + myNodeId)
                                        it.webSocket?.send(gson.toJson(PoANodeUUIDResponse(nodeId = myNodeId)))
                                    });

                    composite.add(
                            teamWsEvents.doOnError { Timber.e(it.localizedMessage) }
                                    .filter { it is WebSocketEvent.StringMessageEvent }
                                    .cast(WebSocketEvent.StringMessageEvent::class.java)
                                    .map { parse(it.text!!) }
                                    .cast(TeamResponse::class.java)
                                    .doOnNext {
                                        Timber.i("Team size updated")
                                        val data = it.data.filterNotNull()
                                        val size = data.size
                                        Timber.d("Command size: " + size)
                                        onTeamSize.onTeamSize(size)
                                        team = data;
                                        if (size > 1) {
                                            startWork(myNodeId, webSocketStringMessageEvents, ws)
                                        }
                                    }
                                    .subscribeOn(Schedulers.io())
                                    .subscribe());

                }

        composite.add(
                webSocketStringMessageEvents
                        .filter { it.second is ResponseRpc }
                        .cast(ResponseRpc::class.java)
                        .doOnNext {
                            if (it.result != null) {
                                Timber.i("Got balance: ${it.result.balance}")
                                balanceListener.onBalance(it.result.balance)
                            } else {
                                balanceListener.onBalance(0);
                            }
                        }.subscribe());

        composite.add(myId.subscribe())

    }

    fun createKey() {
        if (PersistentStorage.getAddress().isEmpty()) {
            val random = SecureRandom()
            val bytes = ByteArray(32)
            random.nextBytes(bytes)
            PersistentStorage.setAddress(Base58.encode(bytes))
        }
    }

    private fun startWork(myId: String, webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>, websocket: WebSocket?) {
        val broadcastMessage = webSocketStringMessageEvents
                .filter { it.second is ReceivedBroadcastMessage }

        val broadcastKeyBlockMessage = webSocketStringMessageEvents
                .filter { it.second is ReceivedBroadcastKeyblockMessage }
                .debounce(MIN_K_BLOCK_PERIOD, TimeUnit.SECONDS)

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
                .doOnNext {
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

                    val microblockMsgString = gson.toJson(microblockMsg)
                    val microblockMsgHash = hash256(microblockMsgString.trim().toByteArray())
                    val microblockMsgHashBase64 = Base64.encodeToString(microblockMsgHash, Base64.DEFAULT)


                    val microblockJson = gson.toJson(microblockResponse)

                    websocket?.send(microblockJson)

                    Handler(Looper.getMainLooper()).post {
                        onMicroblockCountListerer.onMicroblockCountAndLast(++microblocksSoFar, microblockResponse, microblockMsgHashBase64)
                    }

                    currentTransactions = listOf()
                }
                .subscribeOn(Schedulers.io())
                .subscribe())

        composite.add(
                transactionResponses
                        .filter { it.second is TransactionResponse }
                        .map { it.second as TransactionResponse }
                        .doOnNext { Timber.d("Got : ${it.transactions.size} transactions") }
                        .doOnNext {
                            if (currentTransactions.size > TRANSACTIONS_LIMIT_TO_PREVENT_OVERFLOW) {
                                currentTransactions = listOf()
                            }
                            currentTransactions += it.transactions

                            if (currentTransactions.size >= TRANSACTION_COUNT_IN_MICROBLOCK) {
                                Timber.i("START asking for sign")
                                if (team.size > 1) {
                                    for (teamMember in team) {
                                        if (teamMember == myId) {
                                            continue
                                        }
                                        val message = gson.toJson(RequestForSignature(data = currentTransactions.toString()))
                                        if (message != null && myId != null && teamMember != null) {
                                            Timber.d("Sending\nfrom: $myId\nto: $teamMember")
                                            val toJson = gson.toJson(AddressedMessageRequest(msg = message, to = teamMember, from = myId))
                                            websocket?.send(toJson)
                                        }
                                    }
                                } else {
                                    Timber.e("Team is empty")
                                }
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )


        composite.add(
                broadcastKeyBlockMessage
                        .doOnError({ Timber.e(it) })
                        .doOnNext {
                            val response = it.second as ReceivedBroadcastKeyblockMessage;
                            Timber.d("Got key block, start asking for transactions")
                            val keyBlock = response.keyBlock;
                            val keyblockBodyJson = ByteBufferUtils.decode64(keyBlock.body).toString(Charsets.US_ASCII)
                            val kBlockStructure = gson.fromJson(keyblockBodyJson, Array<KBlockStructure>::class.java)
                            keyblockHash = getKeyBlockHash(kBlockStructure.get(0))
                            prev_hash = keyblockHash!!
                            askForNewTransactions(websocket)
                        }.subscribe())

        composite.add(
                addressedMessageResponse
                        .doOnComplete({ Timber.e("Complete") })
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
        val toByteArray = ByteBufferUtils.toByteArray(kBlockStructure)
        val hash256 = hash256(toByteArray)
        return ByteBufferUtils.encode64(hash256)
    }

    fun askForNewTransactions(websocket: WebSocket?) {
        websocket?.send(gson.toJson(TransactionRequest(number = PersistentStorage.getCountTransactionForRequest())));
    }

    private fun getWebSocket(ip: String,
                             port: String): Flowable<WebSocketEvent> {
        val request = Request.Builder().url("ws://$ip:$port").build()

        val managedRxWebSocket = RxWebSocket.createAutoManagedRxWebSocket(request)
        val webSocket = managedRxWebSocket
                .observe()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(10000, 10000))
                .cache()

        return webSocket
    }

    private fun parse(text: String): Any? {
        val containsType = text.contains("type")
        if (containsType) {
            val fromJson = gson.fromJson(text, BasePoAMessage::class.java);
            val type = fromJson.type
            return parse(type, text)
        } else if (text.contains("rpc")) {
            val responseRpc = gson.fromJson(text, ResponseRpc::class.java)
            return responseRpc
        }

        throw IllegalArgumentException("Can't parse type: $text")
    }

    private fun parse(type: String, text: String?): Any? {
        Timber.d("Parsing: ${type}")
        val any = when (type) {
            CommunicationSubjects.Team.name -> gson.fromJson(text, TeamResponse::class.java)
            CommunicationSubjects.PotentialConnects.name -> gson.fromJson(text, ConnectBNResponse::class.java)
            CommunicationSubjects.Connect.name -> gson.fromJson(text, ReconnectRequest::class.java)
            CommunicationSubjects.Broadcast.name -> gson.fromJson(text, ReceivedBroadcastMessage::class.java)
            CommunicationSubjects.KeyBlock.name -> gson.fromJson(text, ReceivedBroadcastKeyblockMessage::class.java)
            CommunicationSubjects.MsgTo.name -> gson.fromJson(text, AddressedMessageResponse::class.java)
            CommunicationSubjects.PoWList.name -> gson.fromJson(text, PowsResponse::class.java)
            CommunicationSubjects.NodeId.name -> gson.fromJson(text, ReconnectResponse::class.java)
            CommunicationSubjects.Transactions.name -> gson.fromJson(text, TransactionResponse::class.java);
            PoACommunicationSubjects.Peek.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWPeekResponse::class.java)
            CommunicationSubjects.ErrorOfConnect.name -> gson.fromJson(text, ErrorResponse::class.java)
            CommunicationSubjects.Microblock.name -> gson.fromJson(text, MicroblockResponse::class.java)
            else -> {
                throw IllegalArgumentException("Can't parse type: $type with messages: $text")
            };
        }
        return any
    }

    fun startAskingForBalance(ip: String, port: String) {
        Timber.i("Starting listening balance at: " + ip + ":" + port)
        val address = Base58.encode(PersistentStorage.getAddress().toByteArray())
        val query = "{\"jsonrpc\":\"2.0\",\"method\":\"getWallet\",\"params\":{\"hash\":\"$address\",\"limit\":-1},\"id\":4}"
        composite.add(
                Flowable.interval(1000, PERIOD_ASK_FOR_BALANCE, TimeUnit.MILLISECONDS)
                        .subscribe {
                            Timber.d("Asking for balance: " + query)
                            Timber.d("Request sent")
                            val sent = nnWs?.send(query)

                            sent?.let {
                                if (!sent) {
                                    Timber.d("Ask for balance did not sent")
                                }
                            }
                        })
    }

    interface onTeamListener {
        fun onTeamSize(size: Int)
    }

    interface onMicroblockCountListener {
        fun onMicroblockCountAndLast(count: Int, microblockResponse: MicroblockResponse, microblockSignature: String)
    }

    interface onConnectedListener {
        fun onConnected(ip: String, port: String);
        fun onDisconnected();
    }

    interface BalanceListener {
        fun onBalance(amount: Int);
    }


}