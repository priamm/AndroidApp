package com.enecuum.androidapp.presentation.view.settings_about_app

import com.arellomobile.mvp.MvpView

interface SettingsAboutAppView : MvpView {
    fun displayVersionNumber(version: String)

}
