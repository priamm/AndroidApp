package com.enecuum.androidapp.presentation.view.settings_terms

import android.text.Spanned
import com.arellomobile.mvp.MvpView

interface SettingsTermsView : MvpView {
    fun displayTerms(termsText: Spanned?)

}
