package com.enecuumwallet.androidapp.presentation.presenter.send_single_tab

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.events.ChangeButtonState
import com.enecuumwallet.androidapp.events.SendAddressChanged
import com.enecuumwallet.androidapp.events.SendAttempt
import com.enecuumwallet.androidapp.models.Currency
import com.enecuumwallet.androidapp.models.SendReceiveMode
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.send_single_tab.SendSingleTabView
import com.enecuumwallet.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuumwallet.androidapp.ui.fragment.send_single_tab.SendSingleTabFragment.Companion.SEND_MODE
import com.enecuumwallet.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class SendSingleTabPresenter : MvpPresenter<SendSingleTabView>() {
    private var isAddressDefined = false
    private var currentAmount = 0
    private var currency: Currency? = null
    private var address = ""

    fun handleArgs(arguments: Bundle?) {
        val sendMode = arguments?.getSerializable(SEND_MODE) as SendReceiveMode
        currency = when (sendMode) {
            SendReceiveMode.Enq -> Currency.Enq
            SendReceiveMode.EnqPlus -> Currency.EnqPlus
        }

        viewState.setupWithAmount(PersistentStorage.getCurrentBalance())
        EventBusUtils.register(this)
        val transaction = arguments.getSerializable(TRANSACTION) as Transaction?
        if (transaction != null)
            viewState.setupWithTransaction(transaction)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    @Subscribe
    fun onSendAddressChanged(event: SendAddressChanged) {
        if (event.currency == currency) {
            viewState.changeAddress(event.newValue)
            //onAddressChanged(event.newValue)
        }
    }

    fun onAmountTextChanged(text: String, skip: Boolean = false) {
        try {
            currentAmount = text.toInt()
        } catch (e: Throwable) {
            currentAmount = 0
            e.printStackTrace()
        }
        if (!skip)
            validate()
    }


    private fun validate() {
        EventBus.getDefault().post(SendAttempt(currency!!, currentAmount, address))
        if (currentAmount >  PersistentStorage.getCurrentBalance().toFloat() || currentAmount <= 0) {
            EventBus.getDefault().post(ChangeButtonState(false))
        } else {
            EventBus.getDefault().post(ChangeButtonState(isAddressDefined))
        }
    }

    fun onAddressChanged(address: String, skip: Boolean = false) {
        this.address = address
        isAddressDefined = address.isNotEmpty()
        if (!skip)
            validate()
    }

    fun refreshButtonState(address: String, amount: String) {
        onAddressChanged(address, true)
        onAmountTextChanged(amount, true)
        validate()
    }
}
