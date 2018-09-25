package com.enecuumwallet.androidapp.presentation.presenter.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.presentation.view.splash.SplashView
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage

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
