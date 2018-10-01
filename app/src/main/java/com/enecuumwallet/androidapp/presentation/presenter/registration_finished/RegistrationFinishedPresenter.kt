package com.enecuumwallet.androidapp.presentation.presenter.registration_finished

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.registration_finished.RegistrationFinishedView
import java.security.KeyPairGenerator
import java.security.SecureRandom

@InjectViewState
class RegistrationFinishedPresenter : MvpPresenter<RegistrationFinishedView>() {
    fun onBackPressed() {
        PersistentStorage.deleteAddress()
        EnecuumApplication.cicerone().router.backTo(ScreenType.Registration.toString())
    }

    fun onNextButtonClick() {
        PersistentStorage.setRegistrationFinished()
        EnecuumApplication.navigateToActivity(ScreenType.Main)
    }

}
