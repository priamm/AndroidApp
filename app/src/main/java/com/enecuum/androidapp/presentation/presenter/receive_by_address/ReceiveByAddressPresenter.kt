package com.enecuum.androidapp.presentation.presenter.receive_by_address

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.BackStackIncreased
import com.enecuum.androidapp.events.ReceiveAddressChanged
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.receive_by_address.ReceiveByAddressView
import com.enecuum.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuum.androidapp.ui.fragment.receive_qr.ReceiveQrFragment
import com.enecuum.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class ReceiveByAddressPresenter : MvpPresenter<ReceiveByAddressView>() {
    private var address = ""
    fun onCreate(arguments: Bundle?) {
        EventBusUtils.register(this)
        val transaction = arguments?.getSerializable(TRANSACTION) as Transaction?
        if(transaction != null) {
            viewState.setupForTransaction(transaction)
        } else {
            val transactionsList = listOf(
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.EnqPlus),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                    Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq)
            )
            viewState.setupNormally()
            viewState.displayTransactionsHistory(transactionsList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    fun onReceiveClick(isMainScreen: Boolean) {
        val bundle = Bundle()
        val realAddress = if(address.isEmpty()) PersistentStorage.getAddress() else address
        bundle.putString(ReceiveQrFragment.ADDRESS, realAddress)
        if(isMainScreen)
            EnecuumApplication.navigateToFragment(FragmentType.ReceiveQr, TabType.Receive, bundle)
        else {
            EnecuumApplication.fragmentCicerone().router.navigateTo(FragmentType.ReceiveQr.toString(), bundle)
            EventBus.getDefault().post(BackStackIncreased())
        }
    }

    @Subscribe
    fun onAddressChanged(event: ReceiveAddressChanged) {
        address = event.newValue
        viewState.changeButtonState(!address.isEmpty())
    }
}
