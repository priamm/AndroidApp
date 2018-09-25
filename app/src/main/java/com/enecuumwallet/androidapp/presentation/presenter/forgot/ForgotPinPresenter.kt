package com.enecuumwallet.androidapp.presentation.presenter.forgot

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.events.ChangeButtonState
import com.enecuumwallet.androidapp.events.DonePressed
import com.enecuumwallet.androidapp.events.PinChanged
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.forgot.ForgotPinView
import com.enecuumwallet.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class ForgotPinPresenter : MvpPresenter<ForgotPinView>() {
    private var currentText = ""
    private var firstPin = ""
    private var currentItem = 0

    fun onCreate() {
        EventBusUtils.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    @Subscribe
    fun onButtonStateChanged(event: ChangeButtonState) {
        viewState.changeButtonState(event.enable)
    }

    @Subscribe
    fun onPinChanged(event: PinChanged) {
        currentText = event.value
    }

    fun onNextClick(currentItem: Int) {
        when(currentItem) {
            0 -> viewState.moveNext()
            1 -> {
                firstPin = currentText
                viewState.moveNext()
            }
            2 -> {
                if(currentText == firstPin) {
                    PersistentStorage.setPin(currentText)
                    EnecuumApplication.cicerone().router.backTo(ScreenType.Registration.toString())
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
    fun onDonePressed(event: DonePressed) {
        onNextClick(currentItem)
    }

}
