package com.enecuum.androidapp.ui.fragment.settings_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.settings_main.SettingsMainView
import com.enecuum.androidapp.presentation.presenter.settings_main.SettingsMainPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuum.androidapp.utils.SettingsInfoUtils
import kotlinx.android.synthetic.main.fragment_settings_main.*

class SettingsMainFragment : NoBackFragment(), SettingsMainView {
    companion object {
        const val TAG = "SettingsMainFragment"

        fun newInstance(): SettingsMainFragment {
            val fragment: SettingsMainFragment = SettingsMainFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SettingsMainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsInfoUtils.setupSettingInfo(changePin, changePinInfo, changePinDescription, {presenter.onChangePinClick()})
        SettingsInfoUtils.setupSettingInfo(backupActions, backupInfo, backupDescription, {presenter.onBackupActionClick()})
        SettingsInfoUtils.setupSettingInfo(aboutApp, aboutInfo, aboutDescription, {presenter.onAboutAppClick()})
    }

    override fun getTitle(): String = getString(R.string.settings)

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }
}
