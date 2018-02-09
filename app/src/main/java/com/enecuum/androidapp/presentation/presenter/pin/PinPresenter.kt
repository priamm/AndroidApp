package com.enecuum.androidapp.presentation.presenter.pin

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.events.ChangeButtonState
import com.enecuum.androidapp.events.PinChanged
import com.enecuum.androidapp.persistent_data.Constants
import com.enecuum.androidapp.presentation.view.pin.PinView
import com.enecuum.androidapp.ui.fragment.pin.PinFragment.Companion.TITLE
import org.greenrobot.eventbus.EventBus

@InjectViewState
class PinPresenter : MvpPresenter<PinView>() {
    fun onPinTextChanged(text: String) {
        viewState.displayPin(text.length)
        EventBus.getDefault().post(ChangeButtonState(text.length == Constants.PIN_COUNT))
        EventBus.getDefault().post(PinChanged(text))
    }

    fun handleArgs(arguments: Bundle?) {
        if(arguments != null) {
            val title = arguments.getString(TITLE)
            viewState.setupWithTitle(title)
        }
    }
}