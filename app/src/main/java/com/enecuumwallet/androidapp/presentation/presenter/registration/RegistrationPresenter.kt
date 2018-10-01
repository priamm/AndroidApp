package com.enecuumwallet.androidapp.presentation.presenter.registration

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.presentation.view.registration.RegistrationView

@InjectViewState
class RegistrationPresenter : MvpPresenter<RegistrationView>() {
    fun newAccountClick() {
        EnecuumApplication.navigateToActivity(ScreenType.NewAccount)
    }

    fun signInClick() {
        EnecuumApplication.navigateToActivity(ScreenType.SignIn)
    }

}
