package com.enecuum.androidapp.presentation.presenter.registration

import application.EnecuumApplication
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.registration.RegistrationView

@InjectViewState
class RegistrationPresenter : MvpPresenter<RegistrationView>() {
    fun newAccountClick() {
        EnecuumApplication.navigateTo(ScreenType.NewAccount)
    }

    fun signInClick() {
        EnecuumApplication.navigateTo(ScreenType.SignIn)
    }

}
