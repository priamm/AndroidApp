package com.enecuum.androidapp.ui.fragment.settings_backup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.settings_backup.SettingsBackupView
import com.enecuum.androidapp.presentation.presenter.settings_backup.SettingsBackupPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.ui.base_ui_primitives.BackTitleFragment
import com.enecuum.androidapp.utils.SettingsInfoUtils
import kotlinx.android.synthetic.main.fragment_settings_backup.*

class SettingsBackupFragment : BackTitleFragment(), SettingsBackupView {
    companion object {
        const val TAG = "SettingsBackupFragment"

        fun newInstance(): SettingsBackupFragment {
            val fragment: SettingsBackupFragment = SettingsBackupFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SettingsBackupPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_backup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsInfoUtils.setupSettingInfo(save, saveInfo, saveDescription, {presenter.onSaveClick()})
        SettingsInfoUtils.setupSettingInfo(copy, copyInfo, copyDescription, {presenter.onCopyClick()})
        SettingsInfoUtils.setupSettingInfo(share, shareInfo, shareDescription, {presenter.onShareClick()})
        setHasOptionsMenu(true)
    }

    override fun getTitle(): String = getString(R.string.backup_actions)

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }
}
