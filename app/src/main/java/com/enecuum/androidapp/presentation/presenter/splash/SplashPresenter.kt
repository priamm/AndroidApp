package com.enecuum.androidapp.presentation.presenter.splash

import application.EnecuumApplication
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.splash.SplashView
import persistent_data.PersistentStorage

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>() {
    fun onCreate() {
        if(PersistentStorage.isRegistrationFinished()) {
            EnecuumApplication.cicerone().router.navigateTo(ScreenType.Main.toString())
        } else {
            EnecuumApplication.cicerone().router.navigateTo(ScreenType.Registration.toString())
        }
    }
}
