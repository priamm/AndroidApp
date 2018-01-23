package com.enecuum.androidapp.presentation.presenter.new_account_qr

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.presentation.view.new_account_qr.NewAccountQrView
import com.github.angads25.filepicker.controller.DialogSelectionListener
import utils.FileSystemUtils
import utils.PermissionUtils

@InjectViewState
class NewAccountQrPresenter : MvpPresenter<NewAccountQrView>(), DialogSelectionListener {
    override fun onSelectedFilePaths(files: Array<out String>?) {

    }

    private var isGenerationStarted = false
    fun beginGenerateKeys() {
        if(isGenerationStarted)
            return
        isGenerationStarted = true
        //TODO: get key from blockchain
        val key = "�r��P���q���[�1GE�E�x�J��;��K+`�{�7������cE��S�6"
        viewState.showQrCode(key)
    }

    fun onSaveClick() {
        if(!PermissionUtils.checkPermissions(PermissionUtils.storagePermissions)) {
            viewState.requestPermissions()
        }
    }

}
