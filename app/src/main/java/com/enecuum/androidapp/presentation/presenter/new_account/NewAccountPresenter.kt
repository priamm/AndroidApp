package com.enecuum.androidapp.presentation.presenter.new_account

import android.content.DialogInterface
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.new_account.NewAccountView
import com.enecuum.androidapp.events.PinBackupFinished
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.PinCreated
import com.enecuum.androidapp.events.SeedBackupFinished
import com.enecuum.androidapp.persistent_data.PersistentStorage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class NewAccountPresenter : MvpPresenter<NewAccountView>(), DialogInterface.OnClickListener {

    private var pin : String = ""
    private var isKeyBackedUp = false
    private var isSeedBackedUp = false

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which) {
            DialogInterface.BUTTON_POSITIVE -> {
                openNextScreen()
            }
        }
    }

    fun onCreate() {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }


    fun onNextClick(currentScreen : Int) {
        when(currentScreen) {
            0 -> viewState.openNextScreen()
            1 -> {
                if (isKeyBackedUp) {
                    viewState.openNextScreen()
                } else {
                    viewState.displaySkipDialog()
                }
            }
            2 -> {
                if (isSeedBackedUp) {
                    openNextScreen()
                } else {
                    viewState.displaySkipDialog()
                }
            }
        }
    }

    private fun openNextScreen() {
        PersistentStorage.setPin(pin)
        EnecuumApplication.navigateTo(ScreenType.RegistrationFinished)
    }

    @Subscribe
    fun onChangeButtonState(event: ChangeButtonState) {
        viewState.changeButtonState(event.enable)
    }

    @Subscribe
    fun onPinBackupFinished(event: PinBackupFinished) {
        isKeyBackedUp = true
    }

    @Subscribe
    fun onSeedBackupFinished(event: SeedBackupFinished) {
        isSeedBackedUp = true
    }

    @Subscribe
    fun onPinCreated(event: PinCreated) {
        pin = event.value
    }
}
