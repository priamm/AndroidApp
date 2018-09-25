package com.enecuumwallet.androidapp.ui.fragment.settings_terms

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.view.settings_terms.SettingsTermsView
import com.enecuumwallet.androidapp.presentation.presenter.settings_terms.SettingsTermsPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackTitleFragment
import kotlinx.android.synthetic.main.fragment_settings_terms.*

class SettingsTermsFragment : BackTitleFragment(), SettingsTermsView {
    companion object {
        const val TAG = "SettingsTermsFragment"

        fun newInstance(): SettingsTermsFragment {
            val fragment: SettingsTermsFragment = SettingsTermsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SettingsTermsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_terms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }

    override fun getTitle(): String = getString(R.string.terms)

    override fun displayTerms(termsText: Spanned?) {
        terms.text = termsText
    }
}
