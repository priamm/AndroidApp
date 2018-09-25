package com.enecuumwallet.androidapp.presentation.presenter.receive_by_address

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.events.BackStackIncreased
import com.enecuumwallet.androidapp.events.ReceiveAddressChanged
import com.enecuumwallet.androidapp.models.SendReceiveMode
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.models.TransactionType
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.receive_by_address.ReceiveByAddressView
import com.enecuumwallet.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuumwallet.androidapp.ui.fragment.receive_qr.ReceiveQrFragment
import com.enecuumwallet.androidapp.utils.EventBusUtils
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
