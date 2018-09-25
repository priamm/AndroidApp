package com.enecuumwallet.androidapp.presentation.presenter.create_seed

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.events.ChangeButtonState
import com.enecuumwallet.androidapp.events.DonePressed
import com.enecuumwallet.androidapp.ui.base_ui_primitives.FileOpeningFragment
import com.enecuumwallet.androidapp.events.SeedBackupFinished
import com.enecuumwallet.androidapp.presentation.view.create_seed.CreateSeedView
import com.enecuumwallet.androidapp.ui.fragment.create_seed.CreateSeedFragment.Companion.RESTORE_MODE
import com.enecuumwallet.androidapp.utils.FileSystemUtils
import com.enecuumwallet.androidapp.utils.PermissionUtils
import com.enecuumwallet.androidapp.utils.Validator
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
