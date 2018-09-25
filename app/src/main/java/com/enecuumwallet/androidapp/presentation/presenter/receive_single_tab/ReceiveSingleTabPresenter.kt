package com.enecuumwallet.androidapp.presentation.presenter.receive_single_tab

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.events.ReceiveAddressChanged
import com.enecuumwallet.androidapp.models.Currency
import com.enecuumwallet.androidapp.models.SendReceiveMode
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.receive_single_tab.ReceiveSingleTabView
import com.enecuumwallet.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuumwallet.androidapp.ui.fragment.receive_single_tab.ReceiveSingleTabFragment.Companion.RECEIVE_MODE
import com.enecuumwallet.androidapp.utils.KeyboardUtils
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
        if(transaction != null) {
            viewState.setupWithTransaction(transaction)
            onAddressTextChanged(transaction.address)
        }
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
