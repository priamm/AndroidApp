package com.enecuumwallet.androidapp.presentation.presenter.change_pin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.events.ChangeButtonState
import com.enecuumwallet.androidapp.events.DonePressed
import com.enecuumwallet.androidapp.events.PinChanged
import com.enecuumwallet.androidapp.persistent_data.Constants
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.change_pin.ChangePinView
import com.enecuumwallet.androidapp.ui.activity.change_pin.ChangePinActivity
import com.enecuumwallet.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class ChangePinPresenter : MvpPresenter<ChangePinView>() {

    private var currentText = ""
    private var firstPin = ""
    private var currentItem = 0

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
                    return
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
                    return
                }
            }
        }
        this.currentItem++
    }

    @Subscribe
    fun onButtonStateChanged(event: ChangeButtonState) {
        viewState.changeButtonState(event.enable)
    }

    @Subscribe
    fun onPinChanged(event: PinChanged) {
        currentText = event.value
    }

    @Subscribe
    fun onDonePressed(event: DonePressed) {
        onNextClick(currentItem)
    }

    fun onCreate() {
        EventBusUtils.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }
}
