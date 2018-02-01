package com.enecuum.androidapp.presentation.presenter.signin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.persistent_data.Constants
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.signin.SignInView

@InjectViewState
class SignInPresenter : MvpPresenter<SignInView>() {
    private var pin = ""
    fun onPinTextChanged(text: String) {
        pin = text
        viewState.displayPin(text.length)
        viewState.changeButtonState(text.length == Constants.PIN_COUNT)
    }

    fun onNextClick() {
        //TODO: check pin
        PersistentStorage.setPin(pin)
        PersistentStorage.setRegistrationFinished()
        EnecuumApplication.navigateToActivity(ScreenType.Main)
    }

    fun onForgotClick() {
        EnecuumApplication.navigateToActivity(ScreenType.ForgotPin)
    }

}
