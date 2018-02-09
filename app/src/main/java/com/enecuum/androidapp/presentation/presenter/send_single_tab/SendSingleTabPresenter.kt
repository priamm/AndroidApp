package com.enecuum.androidapp.presentation.presenter.send_single_tab

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.SendAddressChanged
import com.enecuum.androidapp.events.SendAttempt
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.send_single_tab.SendSingleTabView
import com.enecuum.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuum.androidapp.ui.fragment.send_single_tab.SendSingleTabFragment.Companion.SEND_MODE
import com.enecuum.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class SendSingleTabPresenter : MvpPresenter<SendSingleTabView>() {
    private var totalAmount: Float = 0f
    private var isAddressDefined = false
    private var currentAmount = 0f
    private var currency : Currency? = null
    private var address = ""

    fun handleArgs(arguments: Bundle?) {
        val sendMode = arguments?.getSerializable(SEND_MODE) as SendReceiveMode
        currency = when (sendMode) {
            SendReceiveMode.Enq -> Currency.Enq
            SendReceiveMode.EnqPlus -> Currency.EnqPlus
        }
        totalAmount = PersistentStorage.getCurrencyAmount(currency!!)
        viewState.setupWithAmount(totalAmount)
        EventBusUtils.register(this)
        val transaction = arguments.getSerializable(TRANSACTION) as Transaction?
        if(transaction != null)
            viewState.setupWithTransaction(transaction)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    @Subscribe
    fun onSendAddressChanged(event: SendAddressChanged) {
        if(event.currency == currency) {
            viewState.changeAddress(event.newValue)
            //onAddressChanged(event.newValue)
        }
    }

    fun onAmountTextChanged(text: String, skip:Boolean = false) {
        try {
            val floatRepresentation = text.toFloat()
            currentAmount = floatRepresentation
        } catch (e : Throwable) {
            currentAmount = 0f
            e.printStackTrace()
        }
        if(!skip)
            validate()
    }

    private fun validate() {
        EventBus.getDefault().post(SendAttempt(currency!!, currentAmount, address))
        if (currentAmount > totalAmount || currentAmount <= 0) {
            EventBus.getDefault().post(ChangeButtonState(false))
        } else {
            EventBus.getDefault().post(ChangeButtonState(isAddressDefined))
        }
    }

    fun onAddressChanged(address: String, skip:Boolean = false) {
        this.address = address
        isAddressDefined = address.isNotEmpty()
        if(!skip)
            validate()
    }

    fun refreshButtonState(address: String, amount: String) {
        onAddressChanged(address, true)
        onAmountTextChanged(amount, true)
        validate()
    }
}
