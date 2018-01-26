package com.enecuum.androidapp.presentation.presenter.new_account_pin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.presentation.view.new_account_pin.NewAccountPinView
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.PinCreated
import org.greenrobot.eventbus.EventBus
import com.enecuum.androidapp.utils.Validator

@InjectViewState
class NewAccountPinPresenter : MvpPresenter<NewAccountPinView>() {
    enum class PinString {
        First,
        Second
    }
    private var previousState = false
    fun validateFields(pin1: String, pin2: String) {
        viewState.setPinVisible(!pin1.isEmpty(), PinString.First)
        viewState.setPinVisible(!pin2.isEmpty(), PinString.Second)
        if(!pin1.isEmpty()) {
            viewState.refreshPinState(pin1.length, PinString.First)
        }
        if(!pin2.isEmpty()) {
            viewState.refreshPinState(pin2.length, PinString.Second)
        }
        val nextState: Boolean = Validator.validatePin(pin1) && Validator.validatePin(pin2) && pin1 == pin2
        if(nextState != previousState) {
            EventBus.getDefault().post(ChangeButtonState(nextState))
            previousState = nextState
            if(nextState) {
                EventBus.getDefault().post(PinCreated(pin1))
            }
        }
    }

}
