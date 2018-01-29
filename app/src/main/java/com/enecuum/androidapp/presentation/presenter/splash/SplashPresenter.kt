package com.enecuum.androidapp.presentation.presenter.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.splash.SplashView
import com.enecuum.androidapp.persistent_data.PersistentStorage

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>() {
    fun onCreate() {
        if(PersistentStorage.isRegistrationFinished()) {
            EnecuumApplication.navigateToActivity(ScreenType.Main)
        } else {
            EnecuumApplication.navigateToActivity(ScreenType.Registration)
        }
    }
}
