package com.enecuum.androidapp.presentation.presenter.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.main.MainView

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    fun finishSetup() {
        onHomeClicked()
    }

    fun onHomeClicked() {
        EnecuumApplication.navigateToTab(TabType.Home)
    }

    fun onSendClicked() {
        EnecuumApplication.navigateToTab(TabType.Send)
    }

    fun onReceiveClicked() {
        EnecuumApplication.navigateToTab(TabType.Receive)
    }

    fun onSettingsClicked() {
        EnecuumApplication.navigateToTab(TabType.Settings)
    }

    fun onMiningClick() {
        EnecuumApplication.navigateToActivity(ScreenType.Mining)
    }

    fun onMiningButtonClick() {
        val prevValue = PersistentStorage.isMiningInProgress()
        val nextValue = !prevValue
        PersistentStorage.setMiningInProgress(nextValue)
        viewState.setupMiningPanel(nextValue)
    }

    fun onCreate() {
        viewState.setupMiningPanel(PersistentStorage.isMiningInProgress())
    }
}
