package com.enecuum.androidapp.presentation.presenter.balance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.inherited.models.ConnectPointDescription
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
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Request
import timber.log.Timber
import java.util.concurrent.TimeUnit


@InjectViewState
class BalancePresenter : MvpPresenter<BalanceView>() {

    var poaService: PoaService? = null;

    var composite: CompositeDisposable = CompositeDisposable()

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            if (poaService != null) {
                onMiningToggle(intent)
                onMiningToggle(intent)
            }

            if (poaService == null) {
                onMiningToggle(intent)
            }

        }
    }

    val microblockList = mutableListOf<MicroblockResponse>()
    fun onCreate() {
        //TODO: fill with real values

        LocalBroadcastManager.getInstance(EnecuumApplication.applicationContext())
                .registerReceiver(broadCastReceiver, IntentFilter("reconnectAll"))

//        viewState.displayCurrencyRates(7.999999, 7.999999)
//        viewState.displayBalances(30.0, 30.0)
//        viewState.displayPoints(1.0)
//        viewState.displayKarma(1.0)
//        val microblockList = listOf(
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.EnqPlus),
//                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq)
//        )
//        viewState.displayTransactionsHistory(microblockList)

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
                    Flowable.interval(1000, 5000, TimeUnit.MILLISECONDS)
                            .subscribe {
                                //                                Timber.d("Asking for balance: "+ query)
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

    //    {"jsonrpc":"2.0","result":{"balance":44540},"id":1}
    data class ResponseRpc(val jsonrpc: String, val result: Result?, val id: Int)

    data class Result(val balance: Int)

    fun onMiningToggle(intent: Intent?) {
        val sharedPreferences = EnecuumApplication.applicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

        if (poaService == null) {
            val custom = sharedPreferences.getBoolean(CustomBootNodeFragment.customBN, false)

            val customPath = sharedPreferences.getString(CustomBootNodeFragment.customBNIP, CustomBootNodeFragment.BN_PATH_DEFAULT);
            val customPort = sharedPreferences.getString(CustomBootNodeFragment.customBNPORT, CustomBootNodeFragment.BN_PORT_DEFAULT);
            val path = if (custom) customPath else CustomBootNodeFragment.BN_PATH_DEFAULT
            val port = if (custom) customPort else CustomBootNodeFragment.BN_PORT_DEFAULT
            try {
                if (intent != null) {
                    val firstNNToConnect  = intent.getParcelableExtra("reconnectNN") as ConnectPointDescription
                    reconnect(path, port, firstNNToConnect)
                } else {
                    reconnect(path, port, null)
                }
                viewState.showProgress()
            } catch (t: Throwable) {
                Toast.makeText(EnecuumApplication.applicationContext(), t.localizedMessage, Toast.LENGTH_LONG).show()
            }


        } else {
            poaService?.disconnect()
            poaService = null
            viewState.hideProgress()


        }

        viewState.changeButtonState(poaService == null)


    }

    private fun reconnect(path: String, port: String, nnFirtConnection: ConnectPointDescription?) {
        poaService = PoaService(EnecuumApplication.applicationContext(),
                path,
                port,
                onTeamSize = object : PoaService.onTeamListener {
                    override fun onTeamSize(size: Int) {
                        viewState.displayTeamSize(size);
                    }
                },
                onMicroblockCountListerer = object : PoaService.onMicroblockCountListener {
                    override fun onMicroblockCountAndLast(count: Int, microblockResponse: MicroblockResponse) {
                        microblockList += microblockResponse;
                        viewState.displayTransactionsHistory(microblockList)
                        viewState.displayMicroblocks(10 * count);
                    }
                },
                nnFirtConnection = nnFirtConnection,
                onConnectedListener1 = object : PoaService.onConnectedListener {
                    override fun onConnected(ip: String, port: String) {
                        startLoadingBalance(ip, "1555")
                    }
                }
        )

        poaService?.connect()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(EnecuumApplication.applicationContext())
                .unregisterReceiver(broadCastReceiver)

        super.onDestroy()
    }
}
