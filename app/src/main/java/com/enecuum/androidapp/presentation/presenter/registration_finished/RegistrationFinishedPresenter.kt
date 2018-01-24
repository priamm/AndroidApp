package com.enecuum.androidapp.presentation.presenter.registration_finished

import application.EnecuumApplication
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.registration_finished.RegistrationFinishedView

@InjectViewState
class RegistrationFinishedPresenter : MvpPresenter<RegistrationFinishedView>() {
    fun onBackPressed() {
        EnecuumApplication.cicerone().router.backTo(ScreenType.Registration.toString())
    }

}
