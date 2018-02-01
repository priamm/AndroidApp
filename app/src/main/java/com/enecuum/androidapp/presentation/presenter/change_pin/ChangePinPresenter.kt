package com.enecuum.androidapp.presentation.presenter.change_pin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.persistent_data.Constants
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.change_pin.ChangePinView
import com.enecuum.androidapp.ui.activity.change_pin.ChangePinActivity

@InjectViewState
class ChangePinPresenter : MvpPresenter<ChangePinView>() {

    private var phase = ChangePinActivity.Companion.CurrentPhase.CurrentPin
    private var currentText = ""
    private var firstPin = ""

    fun onCreate() {
        viewState.setupForPhase(ChangePinActivity.Companion.CurrentPhase.CurrentPin)
    }

    fun onPinTextChanged(text: String) {
        currentText = text
        val l = text.length
        viewState.displayPin(l)
        viewState.changeButtonState(l == Constants.PIN_COUNT)
    }

    fun onNextClick() {
        if(validateForCurrentPhase()) {
            phase = when(phase) {
                ChangePinActivity.Companion.CurrentPhase.CurrentPin -> {
                    ChangePinActivity.Companion.CurrentPhase.NewPin
                }
                ChangePinActivity.Companion.CurrentPhase.NewPin -> {
                    ChangePinActivity.Companion.CurrentPhase.ConfirmPin
                }
                ChangePinActivity.Companion.CurrentPhase.ConfirmPin -> {
                    PersistentStorage.setPin(currentText)
                    EnecuumApplication.cicerone().router.exit()
                    return
                }
            }
            viewState.setupForPhase(phase)
        }
    }

    private fun validateForCurrentPhase() : Boolean {
        when(phase) {
            ChangePinActivity.Companion.CurrentPhase.CurrentPin -> {
                if(currentText == PersistentStorage.getPin())
                    return true
                else
                    EnecuumApplication.cicerone().router.showSystemMessage(
                            EnecuumApplication.applicationContext().getString(R.string.wrong_pin))
            }
            ChangePinActivity.Companion.CurrentPhase.NewPin -> {
                firstPin = currentText
                return true
            }
            ChangePinActivity.Companion.CurrentPhase.ConfirmPin -> {
                if(currentText == firstPin) {
                    EnecuumApplication.cicerone().router.showSystemMessage(
                            EnecuumApplication.applicationContext().getString(R.string.pin_changed))
                    return true
                } else
                    EnecuumApplication.cicerone().router.showSystemMessage(
                            EnecuumApplication.applicationContext().getString(R.string.pin_not_equals))
            }
        }
        return false
    }

    fun onBackPressed() {
        phase = when(phase) {
            ChangePinActivity.Companion.CurrentPhase.CurrentPin -> {
                EnecuumApplication.cicerone().router.exit()
                return
            }
            ChangePinActivity.Companion.CurrentPhase.NewPin -> {
                ChangePinActivity.Companion.CurrentPhase.CurrentPin
            }
            ChangePinActivity.Companion.CurrentPhase.ConfirmPin -> {
                ChangePinActivity.Companion.CurrentPhase.NewPin
            }
        }
        viewState.setupForPhase(phase)
    }
}
