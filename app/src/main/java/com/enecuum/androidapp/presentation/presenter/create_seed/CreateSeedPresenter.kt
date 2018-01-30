package com.enecuum.androidapp.presentation.presenter.create_seed

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.ui.base_ui_primitives.FileOpeningFragment
import com.enecuum.androidapp.events.SeedBackupFinished
import com.enecuum.androidapp.presentation.view.create_seed.CreateSeedView
import com.enecuum.androidapp.utils.FileSystemUtils
import com.enecuum.androidapp.utils.PermissionUtils
import com.enecuum.androidapp.utils.Validator
import org.greenrobot.eventbus.EventBus

@InjectViewState
class CreateSeedPresenter : MvpPresenter<CreateSeedView>(), FileOpeningFragment.FileOpeningPresenter {
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
        FileSystemUtils.checkPermissionsAndChooseDir(viewState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        PermissionUtils.checkPermissionsAndRunFunction({onSaveClick()}, requestCode, grantResults)
    }

}
