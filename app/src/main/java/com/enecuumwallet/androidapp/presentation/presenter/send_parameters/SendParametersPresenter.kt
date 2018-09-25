package com.enecuumwallet.androidapp.presentation.presenter.send_parameters

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.events.*
import com.enecuumwallet.androidapp.models.Currency
import com.enecuumwallet.androidapp.models.SendReceiveMode
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.models.TransactionType
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.presentation.view.send_parameters.SendParametersView
import com.enecuumwallet.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuumwallet.androidapp.ui.fragment.send_parameters.SendParametersFragment
import com.enecuumwallet.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.terrakok.cicerone.result.ResultListener

@InjectViewState
class SendParametersPresenter : MvpPresenter<SendParametersView>(), ResultListener {
    companion object {
        const val SCAN_RESULT = 100
    }
    private var currentAmount = 0
    private var currency = Currency.Enq
    private var address: String = ""

    fun onCreate(arguments: Bundle?) {
        EventBusUtils.register(this)
        val transaction = arguments?.getSerializable(TRANSACTION) as Transaction?
        if(transaction != null) {
            viewState.setupForTransaction(transaction)
        } else {
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
            viewState.setupNormally()
            viewState.displayTransactionsHistory(transactionsList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    override fun onResult(resultData: Any?) {
        val scanData = resultData as String?
        if(scanData != null)
            EventBus.getDefault().post(SendAddressChanged(currency, scanData))
    }

    @Subscribe
    fun onButtonStateChanged(event: ChangeButtonState) {
        viewState.changeButtonState(event.enable)
    }

    @Subscribe
    fun onSendAttempt(event: SendAttempt) {
        currency = event.currency
        if(event.amount != null)
            currentAmount = event.amount
        if(event.address != null)
            address = event.address
    }

    fun onSendClick(isMainMode: Boolean) {
        if(currentAmount > 0 && address.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putString(SendParametersFragment.Companion.ADDRESS, address)
            bundle.putInt(SendParametersFragment.Companion.AMOUNT, currentAmount)
            bundle.putSerializable(SendParametersFragment.Companion.CURRENCY, currency)
            bundle.putBoolean(SendParametersFragment.Companion.IS_HISTORY_VISIBLE, isMainMode)
            if(isMainMode) {
                EnecuumApplication.navigateToFragment(FragmentType.SendFinish, TabType.Send, bundle)
            } else {
                EventBus.getDefault().post(BackStackIncreased())
                EnecuumApplication.fragmentCicerone().router.navigateTo(FragmentType.SendFinish.toString(), bundle)
            }
        }
    }

    fun onQrCodeClicked() {
        EnecuumApplication.cicerone().router.setResultListener(SCAN_RESULT, this)
        EnecuumApplication.navigateToActivity(ScreenType.Scan)
    }
}
