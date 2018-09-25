package com.enecuumwallet.androidapp.presentation.presenter.settings_about_app

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.BuildConfig
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.presentation.view.settings_about_app.SettingsAboutAppView
import com.enecuumwallet.androidapp.utils.SystemIntentManager

@InjectViewState
class SettingsAboutAppPresenter : MvpPresenter<SettingsAboutAppView>() {
    fun onCreate() {
        val version = BuildConfig.VERSION_CODE.toString()
        viewState.displayVersionNumber(version)
    }

    fun openSite() {
        SystemIntentManager.openSite("https://enecuum.com")
    }

    fun onTermsClick() {
        SystemIntentManager.openSite("https://enecuum.com/docs/terms.pdf")
    }

    fun onPrivacyClick() {
        SystemIntentManager.openSite("https://enecuum.com/docs/privacy.pdf") //TODO: add real address
    }

    fun onWhitePaperClick() {
        SystemIntentManager.openSite("https://enecuum.com/docs/Enecuum_WP.pdf") //TODO: add real address
    }


}
