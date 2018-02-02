package com.enecuum.androidapp.ui.fragment.settings_about_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.settings_about_app.SettingsAboutAppView
import com.enecuum.androidapp.presentation.presenter.settings_about_app.SettingsAboutAppPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.ui.base_ui_primitives.BackTitleFragment
import kotlinx.android.synthetic.main.fragment_settings_about_app.*

class SettingsAboutAppFragment : BackTitleFragment(), SettingsAboutAppView {
    companion object {
        const val TAG = "SettingsAboutAppFragment"

        fun newInstance(): SettingsAboutAppFragment {
            val fragment: SettingsAboutAppFragment = SettingsAboutAppFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SettingsAboutAppPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_about_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        presenter.onCreate()
        site.setOnClickListener {
            presenter.openSite()
        }
        terms.setOnClickListener {
            presenter.onTermsClick()
        }
        privacy.setOnClickListener {
            presenter.onPrivacyClick()
        }
        whitePaper.setOnClickListener {
            presenter.onWhitePaperClick()
        }
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }

    override fun getTitle(): String = getString(R.string.about_app)

    override fun displayVersionNumber(version: String) {
        versionText.text = version
    }
}
