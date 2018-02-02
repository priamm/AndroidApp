package com.enecuum.androidapp.presentation.presenter.settings_terms

import android.text.Html
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.presentation.view.settings_terms.SettingsTermsView

@InjectViewState
class SettingsTermsPresenter : MvpPresenter<SettingsTermsView>() {
    fun onCreate() {
        val termsText = Html.fromHtml("<h1>Title</h1><p>Require password for purchase or use password to restrict purchace require password for purchase or use password to restrict purchace</p>")
        viewState.displayTerms(termsText)
    }

}
