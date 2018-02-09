package com.enecuum.androidapp.presentation.presenter.receive_single_tab

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.ReceiveAddressChanged
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.receive_single_tab.ReceiveSingleTabView
import com.enecuum.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuum.androidapp.ui.fragment.receive_single_tab.ReceiveSingleTabFragment.Companion.RECEIVE_MODE
import com.enecuum.androidapp.utils.KeyboardUtils
import org.greenrobot.eventbus.EventBus

@InjectViewState
class ReceiveSingleTabPresenter : MvpPresenter<ReceiveSingleTabView>() {
    fun handleArgs(arguments: Bundle?) {
        val sendMode = arguments?.getSerializable(RECEIVE_MODE) as SendReceiveMode
        val currency = when (sendMode) {
            SendReceiveMode.Enq -> Currency.Enq
            SendReceiveMode.EnqPlus -> Currency.EnqPlus
        }
        val totalAmount = PersistentStorage.getCurrencyAmount(currency)
        viewState.setupWithAmount(totalAmount)
        val transaction = arguments.getSerializable(TRANSACTION) as Transaction?
        if(transaction != null)
            viewState.setupWithTransaction(transaction)
    }

    fun onCopyClicked(text: String) {
        if(text.isEmpty())
            return
        KeyboardUtils.copyToClipboard(text)
        EnecuumApplication.cicerone().router.showSystemMessage(EnecuumApplication.applicationContext().getString(R.string.text_copied))
    }

    fun onAddressTextChanged(text: String) {
        EventBus.getDefault().post(ReceiveAddressChanged(text))
    }

}
