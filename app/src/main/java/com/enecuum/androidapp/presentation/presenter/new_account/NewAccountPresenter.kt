package com.enecuum.androidapp.presentation.presenter.new_account

import android.content.DialogInterface
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.presentation.view.new_account.NewAccountView
import com.enecuum.androidapp.events.BackupFinished
import com.enecuum.androidapp.events.ChangeButtonState
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class NewAccountPresenter : MvpPresenter<NewAccountView>(), DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which) {
            DialogInterface.BUTTON_POSITIVE -> {
                openNextScreen()
            }
        }
    }

    private var isKeyBackedUp = false
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
        if(currentScreen == 0)
            viewState.openNextScreen()
        else {
            if(!isKeyBackedUp) {
                viewState.displaySkipDialog()
            } else {
                openNextScreen()
            }
        }
    }

    private fun openNextScreen() {
        EnecuumApplication.cicerone().router.navigateTo(ScreenType.RegistrationFinished.toString())
    }

    @Subscribe
    fun onChangeButtonState(event: ChangeButtonState) {
        viewState.changeButtonState(event.enable)
    }

    @Subscribe
    fun onBackupFinished(event: BackupFinished) {
        isKeyBackedUp = true
    }
}
