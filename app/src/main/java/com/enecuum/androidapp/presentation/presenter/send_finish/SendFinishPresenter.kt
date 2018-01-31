package com.enecuum.androidapp.presentation.presenter.send_finish

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.models.Currency
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
        viewState.setupWithData(address, amount, currency)
        val transactionsList = listOf(
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…")
        )
        viewState.displayTransactionsHistory(transactionsList)
    }

    fun onSendClick() {

    }

}
