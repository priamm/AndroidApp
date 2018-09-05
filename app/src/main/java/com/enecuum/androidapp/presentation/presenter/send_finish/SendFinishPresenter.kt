package com.enecuum.androidapp.presentation.presenter.send_finish

import android.os.Bundle
import android.text.TextUtils
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.inherited.models.ResponseStringRpc
import com.enecuum.androidapp.network.RxWebSocket
import com.enecuum.androidapp.network.WebSocketEvent
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.persistent_data.PersistentStorage.getMasterNode
import com.enecuum.androidapp.presentation.view.send_finish.SendFinishView
import com.enecuum.androidapp.ui.fragment.send_parameters.SendParametersFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import timber.log.Timber
import java.util.*

@InjectViewState
class SendFinishPresenter : MvpPresenter<SendFinishView>() {
    var amount: Float? = 0.0f

    private var address: String? = null

    fun handleArgs(arguments: Bundle?) {
        address = arguments?.getString(SendParametersFragment.Companion.ADDRESS)
        amount = arguments?.getFloat(SendParametersFragment.Companion.AMOUNT)
        val currency = arguments?.getSerializable(SendParametersFragment.Companion.CURRENCY) as Currency
        val isHistoryVisible = arguments.getBoolean(SendParametersFragment.Companion.IS_HISTORY_VISIBLE)
        if (isHistoryVisible) {
//            val transactionsList = listOf(
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.EnqPlus),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
//                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq)
//            )
//            viewState.displayTransactionsHistory(transactionsList)
        } else {
            viewState.hideHistory()
        }
        viewState.setupWithData(address, amount, currency)
    }

    var compositeDisposable = CompositeDisposable();
    var gson: Gson = GsonBuilder().disableHtmlEscaping().create()
    val random = Random()
    fun onSendClick() {
        val masterNode = getMasterNode()
        val webSocket = getWebSocket(masterNode.ip, masterNode.port)



        val owner = PersistentStorage.getWallet()
        val reciever = address
        val sign_s = "ODE0NzgyMTU2NDY0NjUyNjYxODQ1MTI3Nzg3ODc4MTkzNDAxMzk5NjIyOTM5OTk3NTM3MTgwOTI0MjYzODU1Mjc4Nzg2NjEzNjk0MDQ="
        val sign_r = "NzI4Mzc1MTk0MDE2NzM1MjIwMzk2MTExNzc1NzE2MjA4MTQxMDM2NTg3OTkzNzkwMzA0MzU2MDY4MjU5ODU3MDQzNzUzOTczNjk0Nzg="
        val uuid = random.nextInt()
        val queryString = "{\"jsonrpc\":\"2.0\",\"params\":{\"tx\":{\"amount\":${amount},\"uuid\":${uuid},\"owner\":\"$owner\",\"receiver\":\"$reciever\",\"currency\":\"ENQ\",\"sign\":{\"sign_s\":\"$sign_s\",\"sign_r\":\"$sign_r\"}}},\"method\":\"sendTransaction\",\"id\":1}"

        compositeDisposable.add(
                webSocket
                        .filter { it is WebSocketEvent.OpenedEvent }
                        .doOnNext {
                            it.webSocket?.send(queryString)
                        }
                        .doOnNext {
                            viewState.showProgress();
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                        }, {
                            Timber.e(it)
                        }));

        compositeDisposable.add(
                webSocket
                        .filter { it is WebSocketEvent.StringMessageEvent }
                        .map { (it as WebSocketEvent.StringMessageEvent).text}
                        .map { gson.fromJson(it, ResponseStringRpc::class.java) }
                        .doOnNext { viewState.hideProgress(); }
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            val isSent = !TextUtils.isEmpty(it.result)
                            viewState.showTransactionSendStatus(isSent)
                        }, {
                            Timber.e(it)
                        }));

    }

    private fun getWebSocket(ip: String,
                             port: String): Flowable<WebSocketEvent> {
        val request = Request.Builder().url("ws://$ip:$port").build()

        val managedRxWebSocket = RxWebSocket.createAutoManagedRxWebSocket(request)
        val webSocket = managedRxWebSocket
                .observe()
                .doOnError {
                    Timber.e(it)
                }
                .subscribeOn(Schedulers.io())
                .cache()

        return webSocket
    }

}
