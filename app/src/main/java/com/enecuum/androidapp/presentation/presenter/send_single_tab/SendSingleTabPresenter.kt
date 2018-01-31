package com.enecuum.androidapp.presentation.presenter.send_single_tab

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.KeyboardIsVisible
import com.enecuum.androidapp.events.SendAttempt
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.send_single_tab.SendSingleTabView
import com.enecuum.androidapp.ui.fragment.send_single_tab.SendSingleTabFragment
import com.enecuum.androidapp.ui.fragment.send_single_tab.SendSingleTabFragment.Companion.SEND_MODE
import com.enecuum.androidapp.utils.KeyboardUtils
import org.greenrobot.eventbus.EventBus

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
    }

    fun onAmountTextChanged(text: String) {
        try {
            val floatRepresentation = text.toFloat()
            currentAmount = floatRepresentation
        } catch (e : Throwable) {
            currentAmount = 0f
            e.printStackTrace()
        }
        validate()
    }

    private fun validate() {
        if (currentAmount > totalAmount || currentAmount <= 0) {
            EventBus.getDefault().post(ChangeButtonState(false))
        } else {
            if (isAddressDefined) {
                EventBus.getDefault().post(ChangeButtonState(true))
                EventBus.getDefault().post(SendAttempt(currency!!, currentAmount, address))
            } else
                EventBus.getDefault().post(ChangeButtonState(false))
        }
    }

    fun onAddressChanged(address: String) {
        this.address = address
        isAddressDefined = address.isNotEmpty()
        validate()
    }

    fun refreshButtonState(address: String, amount: String) {
        onAddressChanged(address)
        onAmountTextChanged(amount)
    }

    fun onKeyboardVisibilityChanged(rootView: View) {
        EventBus.getDefault().post(KeyboardIsVisible(KeyboardUtils.isKeyboardShown(rootView)))
    }

}
