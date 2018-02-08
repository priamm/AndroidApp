package com.enecuum.androidapp.presentation.presenter.forgot

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.PinChanged
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.forgot.ForgotPinView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class ForgotPinPresenter : MvpPresenter<ForgotPinView>() {
    private var currentText = ""
    private var firstPin = ""

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
                }
            }
        }
    }

}
