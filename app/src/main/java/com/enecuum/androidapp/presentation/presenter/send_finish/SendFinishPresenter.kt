package com.enecuum.androidapp.presentation.presenter.send_finish

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.presentation.view.send_finish.SendFinishView
import com.enecuum.androidapp.ui.fragment.send_parameters.SendParametersFragment

@InjectViewState
class SendFinishPresenter : MvpPresenter<SendFinishView>() {
    fun handleArgs(arguments: Bundle?) {
        val address = arguments?.getString(SendParametersFragment.Companion.ADDRESS)
        val amount = arguments?.getFloat(SendParametersFragment.Companion.AMOUNT)
        val currency = arguments?.getSerializable(SendParametersFragment.Companion.CURRENCY) as Currency
        val isHistoryVisible = arguments.getBoolean(SendParametersFragment.Companion.IS_HISTORY_VISIBLE)
        if(isHistoryVisible) {
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

    fun onSendClick() {

    }

}
