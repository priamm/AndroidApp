package com.enecuumwallet.androidapp.presentation.view.settings_main

import com.arellomobile.mvp.MvpView

interface SettingsMainView : MvpView {
    fun onShowOTPcode(code : String)
    fun onShowError(error : String)
}
