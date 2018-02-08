package com.enecuum.androidapp.presentation.presenter.change_pin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.PinChanged
import com.enecuum.androidapp.persistent_data.Constants
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.change_pin.ChangePinView
import com.enecuum.androidapp.ui.activity.change_pin.ChangePinActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class ChangePinPresenter : MvpPresenter<ChangePinView>() {

    private var currentText = ""
    private var firstPin = ""

    fun onBackPressed() {
        EnecuumApplication.cicerone().router.exit()
    }

    fun onNextClick(currentItem: Int) {
        when(currentItem) {
            0 -> {
                if(currentText == PersistentStorage.getPin()) {
                    viewState.moveNext()
                } else {
                    EnecuumApplication.cicerone().router.showSystemMessage(
                            EnecuumApplication.applicationContext().getString(R.string.wrong_pin))
                }
            }
            1 -> {
               firstPin = currentText
                viewState.moveNext()
            }
            2 -> {
                if(currentText == firstPin) {
                    EnecuumApplication.cicerone().router.showSystemMessage(
                            EnecuumApplication.applicationContext().getString(R.string.pin_changed))
                    PersistentStorage.setPin(currentText)
                    EnecuumApplication.cicerone().router.exit()
                } else {
                    EnecuumApplication.cicerone().router.showSystemMessage(
                            EnecuumApplication.applicationContext().getString(R.string.pin_not_equals))
                }
            }
        }
    }

    @Subscribe
    fun onButtonStateChanged(event: ChangeButtonState) {
        viewState.changeButtonState(event.enable)
    }

    @Subscribe
    fun onPinChanged(event: PinChanged) {
        currentText = event.value
    }

    fun onCreate() {
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }
}
