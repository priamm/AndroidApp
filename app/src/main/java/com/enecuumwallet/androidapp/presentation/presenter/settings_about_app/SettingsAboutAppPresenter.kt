package com.enecuumwallet.androidapp.presentation.presenter.settings_about_app

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.BuildConfig
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.Constants
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
        SystemIntentManager.openSite(Constants.AppConstants.URL_WEB_SITE)
    }

    fun onTermsClick() {
        SystemIntentManager.openSite(Constants.AppConstants.URL_TERMS)
    }

    fun onPrivacyClick() {
        SystemIntentManager.openSite(Constants.AppConstants.URL_PRIVACY)
    }

    fun onWhitePaperClick() {
        SystemIntentManager.openSite(Constants.AppConstants.URL_WHITE_PAPER)
    }


}
