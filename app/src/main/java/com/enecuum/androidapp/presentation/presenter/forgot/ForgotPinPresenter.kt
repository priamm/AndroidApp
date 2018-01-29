package com.enecuum.androidapp.presentation.presenter.forgot

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.forgot.ForgotPinView
import com.enecuum.androidapp.utils.Validator

@InjectViewState
class ForgotPinPresenter : MvpPresenter<ForgotPinView>() {
    fun onSeedTextChanged(text: String) {
        viewState.displayRemainWords(Validator.seedRemainCount(text))
        viewState.setButtonEnabled(Validator.validateSeed(text))
    }

    fun onNextButtonClick() {
        EnecuumApplication.navigateToActivity(ScreenType.RestorePin)
    }

}
