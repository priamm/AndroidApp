package com.enecuumwallet.androidapp.presentation.presenter.settings_backup

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.settings_backup.SettingsBackupView
import com.enecuumwallet.androidapp.ui.base_ui_primitives.FileOpeningFragment
import com.enecuumwallet.androidapp.utils.FileSystemUtils
import com.enecuumwallet.androidapp.utils.KeyboardUtils
import com.enecuumwallet.androidapp.utils.PermissionUtils
import com.enecuumwallet.androidapp.utils.SystemIntentManager

@InjectViewState
class SettingsBackupPresenter : MvpPresenter<SettingsBackupView>(), FileOpeningFragment.FileOpeningPresenter {
    override fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        PermissionUtils.checkPermissionsAndRunFunction({onSaveClick()}, requestCode, grantResults)
    }

    override fun onSelectedFilePaths(files: Array<out String>?) {
        if(files != null && files.isNotEmpty()) {
            EnecuumApplication.cicerone().router.showSystemMessage(
                    EnecuumApplication.applicationContext().getString(R.string.key_backup_finished)
            )
        }
    }

    fun onSaveClick() {
        FileSystemUtils.checkPermissionsAndChooseDir(viewState)
    }

    fun onCopyClick() {
        KeyboardUtils.copyToClipboard(PersistentStorage.getAddress())
        EnecuumApplication.cicerone().router.showSystemMessage(
                EnecuumApplication.applicationContext().getString(R.string.key_copied_to_clipboard))
    }

    fun onShareClick() {
        SystemIntentManager.sendText(PersistentStorage.getAddress())
    }
}
