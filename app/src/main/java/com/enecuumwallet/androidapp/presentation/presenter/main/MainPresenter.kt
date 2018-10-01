package com.enecuumwallet.androidapp.presentation.presenter.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.presentation.view.main.MainView

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    fun finishSetup() {
        onHomeClicked()
    }

    fun onHomeClicked() {
        EnecuumApplication.navigateToTab(TabType.Balance)
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
}
