package com.enecuum.androidapp.presentation.presenter.new_account

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.presentation.view.new_account.NewAccountView
import events.ChangeButtonState
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class NewAccountPresenter : MvpPresenter<NewAccountView>() {
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
    }

    @Subscribe
    fun onChangeButtonState(event : ChangeButtonState) {
        viewState.changeButtonState(event.enable)
    }
}
