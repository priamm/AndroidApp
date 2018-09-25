package com.enecuumwallet.androidapp.presentation.presenter.mining

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.mining.MiningView

@InjectViewState
class MiningPresenter : MvpPresenter<MiningView>() {
    fun onCreate() {
        if (PersistentStorage.isMiningInProgress()) {
            EnecuumApplication.fragmentCicerone().router.replaceScreen(FragmentType.MiningProgress.toString())
        } else {
            EnecuumApplication.fragmentCicerone().router.replaceScreen(FragmentType.MiningStart.toString())
        }
    }

}
