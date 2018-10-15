package com.enecuumwallet.androidapp.ui.activity.testActivity

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Base64
import com.crashlytics.android.Crashlytics
import com.enecuumwallet.androidapp.BuildConfig
import com.enecuumwallet.androidapp.models.inherited.models.*
import com.enecuumwallet.androidapp.models.inherited.models.Sha.hash256
import com.enecuumwallet.androidapp.network.RxWebSocket
import com.enecuumwallet.androidapp.network.WebSocketEvent
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.utils.ByteBufferUtils
import com.enecuumwallet.androidapp.utils.ByteBufferUtils.encode64
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
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.util.concurrent.TimeUnit


class PoaClient(val context: Context,
                val BN_PATH: String,
                val BN_PORT: String,
                val TEAM_WS_IP : String,
                val TEAM_WS_PORT : String,
                val onTeamSizeListener: onTeamListener,
                val onMicroblockCountListerer: onMicroblockCountListener,
                val onConnectedListner: onConnectedListener,
                val balanceListener: BalanceListener) {

    val TRANSACTION_COUNT_IN_MICROBLOCK = 1

    private val PERIOD_ASK_FOR_BALANCE: Long = 30000

    var composite: CompositeDisposable = CompositeDisposable()

    var miningComposite: CompositeDisposable = CompositeDisposable()

    val signature = Signature.getInstance("SHA256withECDSA")
    val keyFactory = KeyFactory.getInstance("ECDSA", org.bouncycastle.jce.provider.BouncyCastleProvider())


    var nnWs: WebSocket? = null
    var teamWs: WebSocket? = null
    var bootNodeWebSocket: WebSocket? = null
    var balanceWebSocket: WebSocket? = null

    var gson: Gson = GsonBuilder().disableHtmlEscaping().create()
    private lateinit var webSocketStringMessageEventsMasterNode: Flowable<Pair<WebSocket?, Any?>>
    private lateinit var bootNodeWebsocketEvents: ConnectableFlowable<WebSocketEvent>
    private lateinit var nnWsEvents: ConnectableFlowable<WebSocketEvent>
    val rsaCipher = RSACipher()
    var currentNodes: List<ConnectPointDescription>? = listOf()

    private var isConnectedVal: Boolean = false

    var isMiningStarted  = false

    var lastBalance = 0

    private var currentNN: ConnectPointDescription? = null

    var currentTransactions: List<Transaction> = listOf()

    private var keyblockHash: String? = null

    private var prev_hash: String = ""

    private var microblocksSoFar = 0

    private val TRANSACTIONS_LIMIT_TO_PREVENT_OVERFLOW: Int = 1000

    private val MIN_K_BLOCK_PERIOD: Long = 10

    private var microBlockWasReady = true

    private fun reconnectToNN(connectPointDescription: ConnectPointDescription) {

        val masterNode = getWebSocket(connectPointDescription.ip, connectPointDescription.port)

        composite.add(masterNode
                .filter {
                    it is WebSocketEvent.OpenedEvent
                }
                .doOnNext {
                    currentNN = connectPointDescription
                    isConnectedVal = true
                    onConnectedListner.onConnected(connectPointDescription.ip, connectPointDescription.port)
                    it.webSocket?.send(gson.toJson(ReconnectAction()))
                    nnWs?.close(1000, "Close")
                    nnWs = it.webSocket

                    PersistentStorage.setMasterNode(connectPointDescription)
                }
                .subscribe())

        composite.add(masterNode.connect())

        webSocketStringMessageEventsMasterNode = masterNode
                .filter {
                    it is WebSocketEvent.StringMessageEvent
                }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map {
                    Pair(it.webSocket, parse(it.text!!))
                }
                .subscribeOn(Schedulers.io())
                .share()

        composite.add(webSocketStringMessageEventsMasterNode
                .filter {
                    it.second is ReconnectResponse
                }
                .doOnNext {
                    Timber.d("Start connect to team node")
                    connectToTeamNode(it)
                }.subscribe())
    }

    private var team: List<String> = mutableListOf()

    private lateinit var myId: String

    fun disconnect() {
        isConnectedVal = false

        listOf(nnWs, bootNodeWebSocket, teamWs, balanceWebSocket)
                .forEach {
                    it?.close(1000, "Client close")
                }

        composite.dispose()
        miningComposite.dispose()

        onTeamSizeListener.onTeamSize(0)
        onConnectedListner.onDisconnected()

        isMiningStarted = false
    }


    fun isConnected(): Boolean {
        return isConnectedVal;
    }

    fun connect() {
        microBlockWasReady = true
        isMiningStarted = true

        Timber.d("Connecting ...")


        composite = CompositeDisposable()
        onConnectedListner.onStartConnecting()
        createKeyIfNeeds()


        bootNodeWebsocketEvents = getWebSocket(BN_PATH, BN_PORT)

        composite.add(bootNodeWebsocketEvents
                .filter {
                    it is WebSocketEvent.OpenedEvent
                }
                .doOnNext {
                    Timber.d("BootNode : open")


                    //request to BootNode
                    val connectBNRequestJson = gson.toJson(ConnectBNRequest())
                    Timber.d("BootNode : request to get master node's $connectBNRequestJson")
                    val masterNodeReqSend = it.webSocket?.send(connectBNRequestJson)

                    val connectApiServicesJson = gson.toJson(GetApiServices())
                    it.webSocket?.send(connectApiServicesJson)

                }
                .subscribe())

        composite.add(bootNodeWebsocketEvents.connect())

        composite.add(
                bootNodeWebsocketEvents
                          .filter {
                              it is WebSocketEvent.StringMessageEvent
                          }
                          .cast(WebSocketEvent.StringMessageEvent::class.java)
                          .map {
                              parse(it.text!!)
                          }
                          .filter({
                              it is ConnectApiServicesResponse
                          })
                          .cast(ConnectApiServicesResponse::class.java)
                          .subscribeOn(Schedulers.io())
                          .subscribe({
                              Timber.d("BootNode : got list of api services : $it.toString()")

                              it.msg.firstOrNull()?.let { balancePoint ->
                                  PersistentStorage.setApiNode(balancePoint)

                                  val balanceWebSocketEvent = getWebSocket(balancePoint.ip, balancePoint.port)

                                  composite.add(balanceWebSocketEvent
                                          .filter { it is WebSocketEvent.OpenedEvent }
                                          .subscribe ({
                                              balanceWebSocket = it.webSocket
                                              Timber.i("Starting listening balance at: " + balancePoint.ip + ":" + balancePoint.port)
                                              startAskingForBalance(balanceWebSocketEvent)
                                          }, {
                                              Crashlytics.log("Balance webSocket : got throwable")
                                              Crashlytics.logException(it)
                                          }))

                                  composite.add(balanceWebSocketEvent.connect())
                              }
                          } , {
                              Timber.d(it)
                              Crashlytics.log("Boot node, StringMessageEvent, got throwable")
                              Crashlytics.logException(it)
                          })
        )

        composite.add(bootNodeWebsocketEvents
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map {
                    parse(it.text!!)
                }
                .filter({
                    it is ConnectBNResponse
                })
                .cast(ConnectBNResponse::class.java)
                .doOnNext {
                    Timber.d("BootNode : got list of master nodes: $it.toString()")
                    currentNodes = it.connects

                    reconnectToNN(it.connects.first())
                }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    private fun connectToTeamNode(masterNodeAndMessage : Pair<WebSocket?, Any?>){
        val teamWsEvents = getWebSocket(TEAM_WS_IP, TEAM_WS_PORT)

        val myNodeId = (masterNodeAndMessage.second as ReconnectResponse).node_id
        val ws = masterNodeAndMessage.first

        myId = myNodeId

        composite.add(
                teamWsEvents
                        .filter {
                            it is WebSocketEvent.OpenedEvent
                        }
                        .subscribe ({

                            teamWs?.close(1000, "Close")

                            teamWs = it.webSocket

                            Timber.d("Team node was connected : myNodeId $myNodeId")


                            if (!TextUtils.isEmpty(myNodeId)) {

                                val versionCode = BuildConfig.VERSION_CODE

                                val response = gson.toJson(PoANodeUUIDResponse(nodeId = myNodeId, version = versionCode))

                                Timber.d("TeamNode : отправка своего id , json = $response")

                                it.webSocket?.send(response)
                            } else {
                                onConnectedListner.onConnectionError("Node id is null")
                                onConnectedListner.doReconnect()
                            }
                        } , {
                            Crashlytics.log("Team webSocket (opened event) : got throwable ${it.localizedMessage}")
                            Timber.e(it.localizedMessage)
                            Crashlytics.logException(it)
                        }))

        composite.add(
                teamWsEvents
                        .filter {
                            it is WebSocketEvent.StringMessageEvent
                        }
                        .cast(WebSocketEvent.StringMessageEvent::class.java)
                        .map { parse(it.text!!) }
                        .filter { it is TeamResponse }
                        .cast(TeamResponse::class.java)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            val data = it.data.filterNotNull()
                            val size = data.size

                            onTeamSizeListener.onTeamSize(size)

                            team = data

                            Timber.d("TeamNode : team size updated, current team size : ${team.size}, team :  $data")

                            if (team.size > 1) {
                                startListeningSignature(webSocketStringMessageEventsMasterNode, ws)
                                startWork(myNodeId, webSocketStringMessageEventsMasterNode, ws)
                            } else {
                                Timber.d("Dispose mining")

                                miningComposite.clear()
                                microBlockWasReady = true
                            }

                        },{
                            Crashlytics.log("Team webSocket (string message event) : got throwable ${it.localizedMessage}")
                            Timber.e(it.localizedMessage)
                            Crashlytics.logException(it)
                        }))


        //wait for k-block fromTeamNode
        composite.add(
                teamWsEvents
                        .doOnError {
                            Timber.e(it.localizedMessage)
                            Crashlytics.logException(it)
                        }
                        .filter {
                            it is WebSocketEvent.StringMessageEvent
                        }
                        .cast(WebSocketEvent.StringMessageEvent::class.java)
                        .map {
                            parse(it.text!!)
                        }
                        .filter{
                            it is ReceivedBroadcastKeyblockMessage
                        }
                        .cast(ReceivedBroadcastKeyblockMessage::class.java)
                        .filter {
                            team.size > 1
                        }
                        .doOnNext {
                            val keyBlock = it.keyBlock

                            val keyblockBodyJson = ByteBufferUtils.decode64(keyBlock.body).toString(Charsets.US_ASCII)

                            val kBlockStructure = gson.fromJson(keyblockBodyJson, Array<KBlockStructure>::class.java)

                            keyblockHash = getKeyBlockHash(kBlockStructure.get(0))


                            if (keyblockHash != prev_hash) {

                                Timber.d("-------------------------------------------------------")
                                Timber.d("TeamNode : got key-block,  hash of it: $keyblockHash, last microblock ready $microBlockWasReady")

                                if (microBlockWasReady) {
                                    askForNewTransactions(ws)
                                    microBlockWasReady = false
                                }

                                prev_hash = keyblockHash!!
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe())

        composite.add(teamWsEvents.connect())
    }

    fun createKeyIfNeeds() {
        if (!PersistentStorage.isKeysExist()) {

            val ecdsAchiper = ECDSAchiper()
            val pair = ecdsAchiper.ecdsaKeyPair

            val privateSindex = pair.private.toString().indexOf("S:")
            val privateSkey = pair.private.toString().slice(privateSindex + 3 until pair.private.toString().length - 1)

            val publicXindex = pair.public.toString().indexOf("X:")
            val publicYindex = pair.public.toString().indexOf("Y:")

            var publicXkey = pair.public.toString().slice(publicXindex + 3 until publicYindex)
            val publicYkey = pair.public.toString().slice(publicYindex + 3 until pair.public.toString().length - 1)

            PersistentStorage.setAddress(Base58.encode(pair.public.encoded))

            publicXkey = publicXkey.trim()

            Timber.d("privateSkey ${privateSkey}")
            Timber.d("privateXkey ${publicXkey}")
            Timber.d("privateYkey ${publicYkey}")

            Timber.d("private key Algorithm : ${pair.private.algorithm}")

            val compressedPK = ECDSAchiper.compressPubKey(BigInteger((publicXkey + publicYkey), 16))

            PersistentStorage.setKeys(privateSkey, publicXkey, publicYkey)
            PersistentStorage.setAddress(compressedPK)


            ECDSAchiper.signData("Hello".toByteArray(), PersistentStorage.getPrivateKey())
        }
    }

    private fun startListeningSignature(webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>, //messages from MasterNode
                                        websocketMasterNode: WebSocket? //MasterNode)
    ){
        Timber.d("Start listening signature")

        val addressedMessageResponse = webSocketStringMessageEvents
                .filter { it.second is AddressedMessageRequestWithSignature }

        val publisher =  PersistentStorage.getWallet() //

        miningComposite.add(
                addressedMessageResponse
                .map {
                    val addressedMessageResponse = it.second as AddressedMessageRequestWithSignature
                    Timber.d("Team size ${team.size}")
                    addressedMessageResponse.msg

                }
                .distinctUntilChanged()
                .buffer(team.size - 1)  //we need singns from all teams memeber except himself
                .doOnError {
                    Crashlytics.logException(it)
                    microBlockWasReady = true
                }

                .doOnNext {

                    if (currentTransactions.isEmpty()) {
                        Timber.w("Microblock is empty, won't send")
                        return@doOnNext
                    }

                    //Got all sings
                    Timber.i("Signed all successfully")

                    val publicKeysFromOtherTeamMembers = mutableListOf<String>() //все подписи от других участников

                    for (responseSignature in it) {

                        if (!TextUtils.isEmpty(responseSignature?.modelSignature?.publicKeyEncoded58)) {
                            responseSignature?.modelSignature?.publicKeyEncoded58?.let { it1 -> publicKeysFromOtherTeamMembers.add(it1) }
                        }
                    }


                    val k_hash = keyblockHash
                    val microblockMsg = MicroblockMsg(
                            Tx = currentTransactions,
                            publisher = publisher,
                            K_hash = k_hash!!,
                            wallets = publicKeysFromOtherTeamMembers)

                    val microblockResponse = MicroblockResponse(
                            microblock = Microblock(microblockMsg,
                                    sign = MicroblockSignature(
                                            sign_r = encode64(PersistentStorage.getPublicXKey()),
                                            sign_s = encode64(PersistentStorage.getPublicYKey()))))

                    val microblockMsgString = gson.toJson(microblockMsg)

                    val microblockMsgHash = hash256(microblockMsgString.trim().toByteArray())
                    val microblockMsgHashBase64 = Base64.encodeToString(microblockMsgHash, Base64.DEFAULT)

                    val microblockJson = gson.toJson(microblockResponse)


                    Timber.d("Microblock was ready, sending it")

                    websocketMasterNode?.send(microblockJson)
                    microBlockWasReady = true
                    currentTransactions = listOf()

                    Handler(Looper.getMainLooper()).post {
                        onMicroblockCountListerer.onMicroblockCountAndLast(microblockResponse, microblockMsgHashBase64)
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }
    private fun startWork(myId: String,
                          webSocketStringMessageEvents: Flowable<Pair<WebSocket?, Any?>>, //messages from MasterNode
                          websocketMasterNode: WebSocket? //MasterNode
    ) {

        Timber.d("Start work")

        val broadcastMessage = webSocketStringMessageEvents
                .filter { it.second is ReceivedBroadcastMessage }

        val addressedMessageResponseWithTransactions = webSocketStringMessageEvents
                .filter { it.second is AddressedMessageRequestWithTransactions }

        val transactionResponses = webSocketStringMessageEvents
                .filter({
                    it.second is TransactionResponse
                })

        val errorMessageResponse = webSocketStringMessageEvents
                .filter { it.second is ErrorResponse }

        miningComposite.add(
                errorMessageResponse
                        .doOnNext {
                            val errorResponse = it.second as ErrorResponse
                            Timber.e("Error: ${errorResponse.comment}")
                            Timber.e("Error: ${errorResponse.Msg}")
                        }
                        .subscribe()
        )


        val trDisposable = transactionResponses
                .filter { it.second is TransactionResponse }
                .map { it.second as TransactionResponse }
                .doOnError {
                    Crashlytics.log("Master node : transactions got error")
                    Crashlytics.logException(it)
                }
                .doOnNext {
                    if (currentTransactions.size > TRANSACTIONS_LIMIT_TO_PREVENT_OVERFLOW) {
                        currentTransactions = listOf()
                    }

                    currentTransactions = it.transactions

                    if (currentTransactions.size >= TRANSACTION_COUNT_IN_MICROBLOCK) {
                        Timber.i("START asking for sign")

                        //send transactions to other team members
                        websocketMasterNode?.let { wsMN ->
                            sendTransactions(wsMN, it.transactions, team)
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe()

        miningComposite.add(trDisposable)

        miningComposite.add(
                addressedMessageResponseWithTransactions
                        .doOnComplete({ Timber.e("Complete") })
                        .filter { it.second is  AddressedMessageRequestWithTransactions}
                        .doOnNext {
                            val before = System.currentTimeMillis()

                            val response = it.second as AddressedMessageRequestWithTransactions

                            if (response.from == myId) {
                                Timber.d("Message from me, skipping...")
                                return@doOnNext
                            }

                            //Transactions of another member
                            val requestForSignature = gson.toJson(response.msg)

                            //we have request For Signature
                            if (requestForSignature != null) {

                                //hash data
                                val hash256 = hash256(requestForSignature)


                                //ECDSA signature


                                /*val p8ks = PKCS8EncodedKeySpec(
                                        Base64.decode(PersistentStorage.getPrivateKey(), Base64.DEFAULT))

                                val privKeyA = keyFactory.generatePrivate(p8ks)

                                signature.initSign(privKeyA)
                                signature.update(requestForSignature.toByteArray())

                                val ecdsaSignature = Base64.encodeToString(signature.sign(), Base64.DEFAULT)*/
                                //ECDSA signature

                                //sign data

                                val enc = rsaCipher.encrypt(hash256) //TODO old signature

                                val myEncodedPublicKey =  PersistentStorage.getWallet()

                                val responseSignature = ResponseSignature(signature = ModelSignature(myId,
                                        hash256, //data hash
                                        enc,  // sign
                                        myEncodedPublicKey))

                                val addressedMessageRequest = AddressedMessageRequestWithTransactionSignature(
                                        to = response.from,
                                        msg = responseSignature,
                                        from = myId)

                                val addressMessageJson = gson.toJson(addressedMessageRequest)

                                //Timber.d("Signed data, send message with signature, json : $addressMessageJson")
                                Timber.d("Signed data, send back to PoA")

                                it.first?.send(gson.toJson(addressedMessageRequest))
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe())
    }

    private fun sendTransactions(websocketMasterNode : WebSocket, transactions : List<Transaction>, team: List<String>) {

        if (team.size > 1) {

            for (teamMember in team) {

                if (teamMember != myId) {
                    try {
                        var toJson = gson.toJson(AddressedMessageRequestWithTransactions(msg = RequestForSignatureList(data = transactions), to = teamMember, from = myId))

                        toJson = toJson.replace("\\", "")

                        //Timber.d("Send message with transactions to another member use MasterNode, json data : $toJson")
                        Timber.d("Send message with transactions to another member use MasterNode")

                        websocketMasterNode.send(toJson)
                    } catch (e : Throwable) {
                        Crashlytics.log("send transactions got throwable")
                        Crashlytics.log("total reconnect")

                        Crashlytics.logException(e)

                        disconnect()
                        connect()
                    }
                }

            }
        } else {
            Timber.d("Team is empty")
        }
    }

    private fun getKeyBlockHash(kBlockStructure: KBlockStructure): String {
        val toByteArray = ByteBufferUtils.toByteArray(kBlockStructure)
        val hash256 = hash256(toByteArray)
        return ByteBufferUtils.encode64(hash256)
    }

    fun askForNewTransactions(websocket: WebSocket?) {
            Timber.d("Master node : ask new transactions : ${gson.toJson(TransactionRequest(number = PersistentStorage.getCountTransactionForRequest()))}")
            websocket?.send(gson.toJson(TransactionRequest(number = PersistentStorage.getCountTransactionForRequest())))
    }

    private fun getWebSocket(ip: String,
                             port: String): ConnectableFlowable<WebSocketEvent> {


        val request = Request.Builder().url("ws://$ip:$port").build()

        val managedRxWebSocket = RxWebSocket.createAutoManagedRxWebSocket(request)

        val webSocket = managedRxWebSocket
                .observe()
                .doOnError {
                    Timber.e(it)
                    onConnectedListner.onConnectionError(it.localizedMessage)
                    onConnectedListner.doReconnect()
                }
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(10000, 10000))
                .share()
                .replay()

        return webSocket
    }

    private fun getNotConnectableWebSocket(ip: String,
                             port: String): Flowable<WebSocketEvent> {


        val request = Request.Builder().url("ws://$ip:$port").build()

        val managedRxWebSocket = RxWebSocket.createAutoManagedRxWebSocket(request)

        val webSocket = managedRxWebSocket
                .observe()
                .doOnError {
                    Timber.e(it)
                    onConnectedListner.onConnectionError(it.localizedMessage)
                    onConnectedListner.doReconnect()
                }
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(10000, 10000))

        return webSocket
    }

    private fun parse(text: String): Any? {

        val containsType = text.contains("type")

        if (containsType) {
            val fromJson = gson.fromJson(text, BasePoAMessage::class.java)
            val type = fromJson.type
            return parse(type, text)
        } else if (text.contains("rpc")) {
            val responseRpc = gson.fromJson(text, ResponseRpc::class.java)
            return responseRpc
        } else {
            return ""
        }

        throw IllegalArgumentException("Can't parse type: $text")
    }

    private fun parse(type: String, text: String?): Any? {
        try {
            val any = when (type) {
                CommunicationSubjects.Team.name -> { gson.fromJson(text, TeamResponse::class.java) }
                CommunicationSubjects.PotentialConnects.name -> gson.fromJson(text, ConnectBNResponse::class.java)
                CommunicationSubjects.Connect.name -> gson.fromJson(text, ReconnectAction::class.java)
                CommunicationSubjects.Broadcast.name -> gson.fromJson(text, ReceivedBroadcastMessage::class.java)
                CommunicationSubjects.KeyBlock.name -> gson.fromJson(text, ReceivedBroadcastKeyblockMessage::class.java)
                CommunicationSubjects.MsgTo.name -> {
                    if (text?.contains("signature") == true) {
                        gson.fromJson(text, AddressedMessageRequestWithSignature::class.java)
                    } else {
                        gson.fromJson(text, AddressedMessageRequestWithTransactions::class.java)
                    }
                }

                CommunicationSubjects.PoWList.name -> gson.fromJson(text, PowsResponse::class.java)
                CommunicationSubjects.NodeId.name -> gson.fromJson(text, ReconnectResponse::class.java)
                CommunicationSubjects.Transactions.name -> gson.fromJson(text, TransactionResponse::class.java);
                PoACommunicationSubjects.Peek.name -> gson.fromJson(text, PoANodeCommunicationTypes.PoWPeekResponse::class.java)
                CommunicationSubjects.ErrorOfConnect.name -> gson.fromJson(text, ErrorResponse::class.java)
                CommunicationSubjects.Microblock.name -> gson.fromJson(text, MicroblockResponse::class.java)
                CommunicationSubjects.getApiServers.name -> {
                    gson.fromJson(text, ConnectApiServicesResponse::class.java)
                }

                else -> {
                    throw IllegalArgumentException("Can't parse type: $type with messages: $text")
                }
            }

            //Timber.d("Parsing msg with type : $type , json data $text")

            return any
        } catch (e : Throwable) {
            Timber.d("Parse : can't parse message, type: $type")
            Crashlytics.log("Parse : can't parse message, type: $type")
            Crashlytics.logException(e)
            return ""
        }

    }

    fun startAskingForBalance(balanceWebSocketEvent: Flowable<WebSocketEvent>) {
        composite.add(
                balanceWebSocketEvent
                        .filter { it is WebSocketEvent.StringMessageEvent }
                        .cast(WebSocketEvent.StringMessageEvent::class.java)
                        .map { parse(it.text!!) }
                        .doOnError {
                            Crashlytics.logException(it)
                        }
                        .cast(ResponseRpc::class.java)
                        .subscribe({
                            if (it.result != null) {
                                //Timber.i("Got balance: ${it.result.balance}")
                                balanceListener.onBalance(it.result.balance)
                                lastBalance = it.result.balance
                            } else {
                                balanceListener.onBalance(0)
                                lastBalance = 0
                            }
                        },{
                            Timber.d(it)
                            Crashlytics.log("Balance web socket got throwable")
                            Crashlytics.logException(it)
                        }))


        val address =  PersistentStorage.getWallet()
        val query = "{\"jsonrpc\":\"2.0\",\"method\":\"getWallet\",\"params\":{\"hash\":\"$address\",\"limit\":-1},\"id\":4}"
        composite.add(
                Flowable.interval(1000, PERIOD_ASK_FOR_BALANCE, TimeUnit.MILLISECONDS)
                        .subscribe ({

                            //Timber.d("Ask for balance")
                            val sent = balanceWebSocket?.send(query)
                            sent?.let {
                                if (!sent) {
                                    Timber.d("Ask for balance did not sent")
                                }
                            }
                        }, {
                            Crashlytics.log("Balance web socket : got throwable")
                            Crashlytics.logException(it)
                        }))
    }

    interface onTeamListener {
        fun onTeamSize(size: Int)
    }

    interface onMicroblockCountListener {
        fun onMicroblockCountAndLast(microblockResponse: MicroblockResponse, microblockSignature: String)
    }

    interface onConnectedListener {
        fun onStartConnecting()
        fun onConnected(ip: String, port: String)
        fun onDisconnected()
        fun doReconnect()
        fun onConnectionError(localizedMessage: String)
    }

    interface BalanceListener {
        fun onBalance(amount: Int)
    }
}