package com.enecuum.androidapp.presentation.presenter.balance

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.inherited.models.MicroblockResponse
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.network.RxWebSocket
import com.enecuum.androidapp.network.WebSocketEvent
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.balance.BalanceView
import com.enecuum.androidapp.ui.activity.testActivity.Base58
import com.enecuum.androidapp.ui.activity.testActivity.CustomBootNodeFragment
import com.enecuum.androidapp.ui.activity.testActivity.PoaService
import com.google.gson.Gson
import io.fabric.sdk.android.Fabric.isInitialized
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Request
import timber.log.Timber
import java.util.concurrent.TimeUnit


@InjectViewState
class BalancePresenter : MvpPresenter<BalanceView>() {

    private lateinit var poaService: PoaService

    var composite: CompositeDisposable = CompositeDisposable()

    val sharedPreferences = EnecuumApplication.applicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

    val microblockList = hashMapOf<String, MicroblockResponse>()

    val custom = sharedPreferences.getBoolean(CustomBootNodeFragment.customBN, false)
    val customPath = sharedPreferences.getString(CustomBootNodeFragment.customBNIP, CustomBootNodeFragment.BN_PATH_DEFAULT);
    val customPort = sharedPreferences.getString(CustomBootNodeFragment.customBNPORT, CustomBootNodeFragment.BN_PORT_DEFAULT);
    val path = if (custom) customPath else CustomBootNodeFragment.BN_PATH_DEFAULT
    val port = if (custom) customPort else CustomBootNodeFragment.BN_PORT_DEFAULT

    fun onCreate() {
        poaService = PoaService(EnecuumApplication.applicationContext(),
                path,
                port,
                onTeamSize = object : PoaService.onTeamListener {
                    override fun onTeamSize(size: Int) {
                        Handler(Looper.getMainLooper()).post {
                            viewState.displayTeamSize(size);
                        }
                    }
                },
                onMicroblockCountListerer = object : PoaService.onMicroblockCountListener {
                    override fun onMicroblockCountAndLast(count: Int, microblockResponse: MicroblockResponse, microblockSignature: String) {
                        microblockList.put(microblockSignature, microblockResponse);
                        viewState.displayTransactionsHistory(microblockList.keys.toList())
                        viewState.displayMicroblocks(10 * count);
                    }
                },
                onConnectedListener1 = object : PoaService.onConnectedListener {
                    override fun onDisconnected() {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(true)
                            viewState.hideProgress()
                        }
                    }

                    override fun onConnected(ip: String, port: String) {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(false)
                            viewState.showProgress()
                        }
//                        startLoadingBalance(ip, "1555")
                    }
                }
        )
    }

    fun onTokensClick() {
        EnecuumApplication.navigateToFragment(FragmentType.Tokens, TabType.Home)
    }

    private fun getWebSocket(ip: String,
                             port: String): RxWebSocket {
        val request = Request.Builder().url("ws://$ip:$port").build()
        val webSocket = RxWebSocket.createAutoManagedRxWebSocket(request)
        return webSocket
    }

    val gson = Gson()

    fun startLoadingBalance(ip: String, port: String) {

        composite.clear()

        Timber.i("Starting listening balance at: " + ip + ":" + port)
        val webSocket = getWebSocket(ip, port).observe()
                .observeOn(AndroidSchedulers.mainThread())
                .publish()

        val address = Base58.encode(PersistentStorage.getAddress().toByteArray())
        val query = "{\"jsonrpc\":\"2.0\",\"method\":\"getWallet\",\"params\":{\"hash\":\"$address\",\"limit\":-1},\"id\":4}"
        val webSocketForBalance = webSocket.autoConnect(2)
        composite.add(webSocketForBalance
                .filter { it is WebSocketEvent.OpenedEvent }
                .subscribe {
                    val webSocket = it.webSocket;
                    Flowable.interval(1000, 60000, TimeUnit.MILLISECONDS)
                            .subscribe {
                                Timber.d("Asking for balance: " + query)
                                webSocket?.send(query)
                            }
                })

        composite.add(webSocketForBalance
                .filter { it is WebSocketEvent.StringMessageEvent }
                .subscribe {
                    val stringMessageEvent = it as WebSocketEvent.StringMessageEvent
                    val responseRpc = gson.fromJson(stringMessageEvent.text, ResponseRpc::class.java)
                    if (responseRpc.result != null) {
                        Timber.i("Got balance: ${responseRpc.result.balance}")
                        viewState.setBalance(responseRpc.result.balance)
                    } else {
                        viewState.setBalance(0);
                    }
                })


    }

    data class ResponseRpc(val jsonrpc: String, val result: Result?, val id: Int)

    data class Result(val balance: Int)

    fun onMiningToggle() {
        if (::poaService.isInitialized) {
            if (poaService.isConnected()) {
                poaService.disconnect()
            } else {
                poaService.connect()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::poaService.isInitialized) {
            if (poaService.isConnected()) {
                poaService.disconnect()
            }
        }
    }
}
