package com.enecuumwallet.androidapp.utils

import android.app.Activity
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.ui.base_ui_primitives.FileOpener
import com.github.angads25.filepicker.controller.DialogSelectionListener
import com.github.angads25.filepicker.model.DialogConfigs
import com.github.angads25.filepicker.model.DialogProperties
import com.github.angads25.filepicker.view.FilePickerDialog

/**
 * Created by oleg on 23.01.18.
 */
object FileSystemUtils {
    fun chooseDirectory(activity: Activity, listener : DialogSelectionListener) {
        val properties = DialogProperties()
        properties.selection_mode = DialogConfigs.SINGLE_MODE
        properties.selection_type = DialogConfigs.DIR_SELECT
        val dialog = FilePickerDialog(activity, properties)
        dialog.setTitle(activity.getString(R.string.select_dir))
        dialog.setDialogSelectionListener(listener)
        dialog.show()
    }

    fun checkPermissionsAndChooseDir(fileOpener: FileOpener) {
        if(!PermissionUtils.checkPermissions(PermissionUtils.storagePermissions)) {
            fileOpener.requestPermissions()
            return
        }
        fileOpener.chooseDirectory()
    }
}