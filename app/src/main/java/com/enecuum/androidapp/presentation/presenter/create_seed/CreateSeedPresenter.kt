package com.enecuum.androidapp.presentation.presenter.create_seed

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.events.SeedBackupFinished
import com.enecuum.androidapp.presentation.view.create_seed.CreateSeedView
import com.enecuum.androidapp.utils.FileSystemUtils
import com.enecuum.androidapp.utils.PermissionUtils
import com.enecuum.androidapp.utils.Validator
import com.github.angads25.filepicker.controller.DialogSelectionListener
import org.greenrobot.eventbus.EventBus

@InjectViewState
class CreateSeedPresenter : MvpPresenter<CreateSeedView>(), DialogSelectionListener {
    override fun onSelectedFilePaths(files: Array<out String>?) {
        if(files != null && files.isNotEmpty()) {
            EventBus.getDefault().post(SeedBackupFinished())
        }
    }

    fun validateSeed(text: String) {
        viewState.displayRemainWords(Validator.seedRemainCount(text))
        viewState.setButtonEnabled(Validator.validateSeed(text))
    }

    fun onSaveClick() {
        if(!PermissionUtils.checkPermissions(PermissionUtils.storagePermissions)) {
            viewState.requestPermissions()
            return
        }
        viewState.chooseSeedDirectory()
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if(requestCode != PermissionUtils.PermissionsRequestCode)
            return
        if(PermissionUtils.handleGrantResults(grantResults)) {
            onSaveClick()
        }
    }

}
