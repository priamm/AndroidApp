package com.enecuum.androidapp.presentation.presenter.send_finish

import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.network.RxWebSocket
import com.enecuum.androidapp.network.WebSocketEvent
import com.enecuum.androidapp.persistent_data.PersistentStorage.getMasterNode
import com.enecuum.androidapp.presentation.view.send_finish.SendFinishView
import com.enecuum.androidapp.ui.activity.testActivity.RetryWithDelay
import com.enecuum.androidapp.ui.fragment.send_parameters.SendParametersFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import timber.log.Timber

@InjectViewState
class SendFinishPresenter : MvpPresenter<SendFinishView>() {
    fun handleArgs(arguments: Bundle?) {
        val address = arguments?.getString(SendParametersFragment.Companion.ADDRESS)
        val amount = arguments?.getFloat(SendParametersFragment.Companion.AMOUNT)
        val currency = arguments?.getSerializable(SendParametersFragment.Companion.CURRENCY) as Currency
        val isHistoryVisible = arguments.getBoolean(SendParametersFragment.Companion.IS_HISTORY_VISIBLE)
        if (isHistoryVisible) {
            val transactionsList = listOf(
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.EnqPlus),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq)
            )
            viewState.displayTransactionsHistory(transactionsList)
        } else {
            viewState.hideHistory()
        }
        viewState.setupWithData(address, amount, currency)
    }

    var gson: Gson = GsonBuilder().disableHtmlEscaping().create()
    fun onSendClick() {
        val masterNode = getMasterNode()
        val webSocket = getWebSocket(masterNode.ip, masterNode.port)

        webSocket
                .filter { it is WebSocketEvent.OpenedEvent }
                .doOnNext {
                    val request = gson.toJson("");
                    it.webSocket?.send(request)
                }
                .subscribeOn(Schedulers.io())
                .subscribe({

                },{
                    Timber.e(it)
                })

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
