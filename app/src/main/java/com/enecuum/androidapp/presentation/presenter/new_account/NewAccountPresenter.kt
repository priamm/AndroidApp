package com.enecuum.androidapp.presentation.presenter.new_account

import android.content.DialogInterface
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.*
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.new_account.NewAccountView
import com.enecuum.androidapp.persistent_data.PersistentStorage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class NewAccountPresenter : MvpPresenter<NewAccountView>(), DialogInterface.OnClickListener {

    private var pin : String = ""
    private var firstPin : String = ""
    private var isKeyBackedUp = false
    private var isSeedBackedUp = false
    private var currentPage = 0

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which) {
            DialogInterface.BUTTON_POSITIVE -> {
                if(currentPage < 3)
                    viewState.moveNext()
                else
                    openNextScreen()
            }
        }
    }

    fun onCreate() {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        currentPage = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }


    fun onNextClick(currentScreen : Int) {
        when(currentScreen) {
            0 -> {
                firstPin = pin
                viewState.moveNext()
            }
            1 -> {
                if(firstPin != pin) {
                    EnecuumApplication.cicerone().router.showSystemMessage(
                            EnecuumApplication.applicationContext().getString(R.string.pin_not_equals)
                    )
                } else {
                    PersistentStorage.setPin(pin)
                    viewState.moveNext()
                }
            }
            2 -> {
                if (isKeyBackedUp) {
                    viewState.moveNext()
                } else {
                    viewState.displaySkipDialog()
                }
            }
            3 -> {
                if (isSeedBackedUp) {
                    openNextScreen()
                } else {
                    viewState.displaySkipDialog()
                }
            }
        }
        currentPage = currentScreen
    }

    private fun openNextScreen() {
        PersistentStorage.setPin(pin)
        EnecuumApplication.navigateToActivity(ScreenType.RegistrationFinished)
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
    fun onPinChanged(event: PinChanged) {
        pin = event.value
    }

    fun onBackPressed() {
        PersistentStorage.deleteAddress()
        PersistentStorage.deletePin()
    }
}
