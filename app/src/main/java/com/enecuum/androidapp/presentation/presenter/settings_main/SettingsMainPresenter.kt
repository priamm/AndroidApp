package com.enecuum.androidapp.presentation.presenter.settings_main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.presentation.view.settings_main.SettingsMainView

@InjectViewState
class SettingsMainPresenter : MvpPresenter<SettingsMainView>() {
    fun onChangePinClick() {
        EnecuumApplication.navigateToActivity(ScreenType.ChangePin)
    }

    fun onBackupActionClick() {
        EnecuumApplication.navigateToFragment(FragmentType.SettingsBackup, TabType.Settings)
    }

    fun onAboutAppClick() {
        EnecuumApplication.navigateToFragment(FragmentType.SettingsAboutApp, TabType.Settings)
    }

    fun onCustomBNAppClick() {
        EnecuumApplication.navigateToFragment(FragmentType.CustomBootNode, TabType.Settings)
    }

    fun onMyWalletClick() {
        EnecuumApplication.navigateToFragment(FragmentType.MyWallet, TabType.Settings)
    }
}
