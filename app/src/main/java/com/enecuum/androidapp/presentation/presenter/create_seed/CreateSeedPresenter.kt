package com.enecuum.androidapp.presentation.presenter.create_seed

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.DonePressed
import com.enecuum.androidapp.ui.base_ui_primitives.FileOpeningFragment
import com.enecuum.androidapp.events.SeedBackupFinished
import com.enecuum.androidapp.presentation.view.create_seed.CreateSeedView
import com.enecuum.androidapp.ui.fragment.create_seed.CreateSeedFragment.Companion.RESTORE_MODE
import com.enecuum.androidapp.utils.FileSystemUtils
import com.enecuum.androidapp.utils.PermissionUtils
import com.enecuum.androidapp.utils.Validator
import org.greenrobot.eventbus.EventBus

@InjectViewState
class CreateSeedPresenter : MvpPresenter<CreateSeedView>(), FileOpeningFragment.FileOpeningPresenter {
    private var isRestoreMode = false
    private var text: String = ""

    override fun onSelectedFilePaths(files: Array<out String>?) {
        if(files != null && files.isNotEmpty()) {
            EventBus.getDefault().post(SeedBackupFinished())
        }
    }

    fun validateSeed(text: String) {
        this.text = text
        viewState.displayRemainWords(Validator.seedRemainCount(text))
        val isValid = Validator.validateSeed(text)
        if(isRestoreMode) {
            EventBus.getDefault().post(ChangeButtonState(isValid))
        } else {
            viewState.setButtonEnabled(isValid)
        }
    }

    fun onSaveClick() {
        FileSystemUtils.checkPermissionsAndChooseDir(viewState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        PermissionUtils.checkPermissionsAndRunFunction({onSaveClick()}, requestCode, grantResults)
    }

    fun handleArgs(arguments: Bundle?) {
        if(arguments != null) {
            isRestoreMode = arguments.getBoolean(RESTORE_MODE)
            if(isRestoreMode) {
                viewState.setupRestoreMode()
            }
        }
    }

    fun onDonePressed(): Boolean {
        if(Validator.validateSeed(text)) {
            EventBus.getDefault().post(DonePressed())
            return true
        }
        return false
    }

}
