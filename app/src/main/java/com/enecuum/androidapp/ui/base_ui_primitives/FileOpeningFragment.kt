package com.enecuum.androidapp.ui.base_ui_primitives

import com.enecuum.androidapp.utils.FileSystemUtils
import com.enecuum.androidapp.utils.PermissionUtils
import com.github.angads25.filepicker.controller.DialogSelectionListener

/**
 * Created by oleg on 26.01.18.
 */
abstract class FileOpeningFragment : TitleFragment(), FileOpener {

    interface FileOpeningPresenter : DialogSelectionListener {
        fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray)
    }

    abstract fun getFilePresenter() : FileOpeningPresenter

    override fun requestPermissions() {
        if(activity == null)
            return
        PermissionUtils.requestPermissions(this, PermissionUtils.storagePermissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getFilePresenter().onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun chooseDirectory() {
        FileSystemUtils.chooseDirectory(activity!!, getFilePresenter())
    }
}