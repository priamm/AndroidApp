package com.enecuumwallet.androidapp.presentation.presenter.pin

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.events.ChangeButtonState
import com.enecuumwallet.androidapp.events.DonePressed
import com.enecuumwallet.androidapp.events.PinChanged
import com.enecuumwallet.androidapp.persistent_data.Constants
import com.enecuumwallet.androidapp.presentation.view.pin.PinView
import com.enecuumwallet.androidapp.ui.fragment.pin.PinFragment.Companion.TITLE
import org.greenrobot.eventbus.EventBus

@InjectViewState
class PinPresenter : MvpPresenter<PinView>() {
    private var text:String? = null
    fun onPinTextChanged(text: String) {
        this.text = text
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

    fun onDonePressed(): Boolean {
        if(text?.length == Constants.PIN_COUNT) {
            EventBus.getDefault().post(DonePressed())
            return true
        }
        return false
    }
}
