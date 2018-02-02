package com.enecuum.androidapp.presentation.presenter.settings_about_app

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.BuildConfig
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.presentation.view.settings_about_app.SettingsAboutAppView
import com.enecuum.androidapp.utils.SystemIntentManager

@InjectViewState
class SettingsAboutAppPresenter : MvpPresenter<SettingsAboutAppView>() {
    fun onCreate() {
        val version = BuildConfig.VERSION_CODE.toString()
        viewState.displayVersionNumber(version)
    }

    fun openSite() {
        SystemIntentManager.openSite(EnecuumApplication.applicationContext().getString(R.string.enecuum_com))
    }

    fun onTermsClick() {
        EnecuumApplication.navigateToFragment(FragmentType.SettingsTerms, TabType.Settings)
    }

    fun onPrivacyClick() {
        SystemIntentManager.openSite("https://enecuum.com/privacy.html") //TODO: add real address
    }

    fun onWhitePaperClick() {
        SystemIntentManager.openSite("https://enecuum.com/whitepaper.html") //TODO: add real address
    }


}
