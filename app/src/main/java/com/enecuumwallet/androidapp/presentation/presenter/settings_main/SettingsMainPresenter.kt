package com.enecuumwallet.androidapp.presentation.presenter.settings_main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.presentation.view.settings_main.SettingsMainView

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
