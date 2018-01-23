package com.enecuum.androidapp.presentation.presenter.new_account_pin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.presentation.view.new_account_pin.NewAccountPinView
import events.ChangeButtonState
import org.greenrobot.eventbus.EventBus
import utils.Validator

@InjectViewState
class NewAccountPinPresenter : MvpPresenter<NewAccountPinView>() {
    private var previousState = false
    fun validateFields(pin1: String, pin2: String) {
        val nextState: Boolean = Validator.validatePin(pin1) && Validator.validatePin(pin2) && pin1 == pin2
        if(nextState != previousState) {
            EventBus.getDefault().post(ChangeButtonState(nextState))
            previousState = nextState
        }
    }

}
