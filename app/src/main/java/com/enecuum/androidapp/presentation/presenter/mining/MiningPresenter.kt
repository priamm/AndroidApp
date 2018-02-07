package com.enecuum.androidapp.presentation.presenter.mining

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.mining.MiningView

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
