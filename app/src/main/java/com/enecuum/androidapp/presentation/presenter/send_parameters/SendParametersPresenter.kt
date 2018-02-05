package com.enecuum.androidapp.presentation.presenter.send_parameters

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.*
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.presentation.view.send_parameters.SendParametersView
import com.enecuum.androidapp.ui.fragment.send_parameters.SendParametersFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.terrakok.cicerone.result.ResultListener

@InjectViewState
class SendParametersPresenter : MvpPresenter<SendParametersView>(), ResultListener {
    companion object {
        const val SCAN_RESULT = 100
    }
    private var currentAmount = 0f
    private var currency = Currency.Enq
    private var address: String = ""

    fun onCreate() {
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onResult(resultData: Any?) {
        val scanData = resultData as String?
        if(scanData != null)
            EventBus.getDefault().post(SendAddressChanged(currency, scanData))
    }

    @Subscribe
    fun onKeyboardVisibilityChanged(event: KeyboardIsVisible) {
        viewState.handleKeyboardVisibility(event.isVisible)
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

    fun onSendClick() {
        if(currentAmount > 0 && address.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putString(SendParametersFragment.Companion.ADDRESS, address)
            bundle.putFloat(SendParametersFragment.Companion.AMOUNT, currentAmount)
            bundle.putSerializable(SendParametersFragment.Companion.CURRENCY, currency)
            EnecuumApplication.navigateToFragment(FragmentType.SendFinish, TabType.Send, bundle)
        }
    }

    fun onQrCodeClicked() {
        EnecuumApplication.cicerone().router.setResultListener(SCAN_RESULT, this)
        EnecuumApplication.navigateToActivity(ScreenType.Scan)
    }
}
