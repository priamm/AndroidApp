package com.enecuum.androidapp.presentation.presenter.receive_by_address

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.events.KeyboardIsVisible
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.presentation.view.receive_by_address.ReceiveByAddressView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class ReceiveByAddressPresenter : MvpPresenter<ReceiveByAddressView>() {
    fun onCreate() {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        val transactionsList = listOf(
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…")
        )
        viewState.displayTransactionsHistory(transactionsList)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    fun onQrClick() {

    }

    @Subscribe
    fun onKeyboardVisibilityChanged(event: KeyboardIsVisible) {
        viewState.handleKeyboardVisibility(event.isVisible)
    }
}
