package com.enecuum.androidapp.presentation.presenter.restore_pin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.PinCreated
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.restore_pin.RestorePinView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class RestorePinPresenter : MvpPresenter<RestorePinView>() {
    private var pin : String = ""

    fun onCreate() {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    fun onNextClick() {
        PersistentStorage.setPin(pin)
        EnecuumApplication.cicerone().router.backTo(ScreenType.Registration.toString())
    }

    @Subscribe
    fun onChangeButtonEvent(event: ChangeButtonState) {
        viewState.setButtonEnabled(event.enable)
    }

    @Subscribe
    fun onPinCreated(event: PinCreated) {
        pin = event.value
    }
}
