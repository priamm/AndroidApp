package com.enecuum.androidapp.presentation.presenter.receive_by_address

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.KeyboardIsVisible
import com.enecuum.androidapp.events.ReceiveAddressChanged
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.receive_by_address.ReceiveByAddressView
import com.enecuum.androidapp.ui.fragment.receive_qr.ReceiveQrFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class ReceiveByAddressPresenter : MvpPresenter<ReceiveByAddressView>() {
    private var address = ""
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
        val bundle = Bundle()
        val realAddress = if(address.isEmpty()) PersistentStorage.getAddress() else address
        bundle.putString(ReceiveQrFragment.ADDRESS, realAddress)
        EnecuumApplication.navigateToFragment(FragmentType.ReceiveQr, TabType.Receive, bundle)
    }

    @Subscribe
    fun onKeyboardVisibilityChanged(event: KeyboardIsVisible) {
        viewState.handleKeyboardVisibility(event.isVisible)
    }

    @Subscribe
    fun onAddressChanged(event: ReceiveAddressChanged) {
        address = event.newValue
    }
}
