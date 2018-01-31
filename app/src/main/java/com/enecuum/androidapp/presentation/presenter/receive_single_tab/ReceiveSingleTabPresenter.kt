package com.enecuum.androidapp.presentation.presenter.receive_single_tab

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.KeyboardIsVisible
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.presentation.view.receive_single_tab.ReceiveSingleTabView
import com.enecuum.androidapp.ui.fragment.receive_single_tab.ReceiveSingleTabFragment.Companion.RECEIVE_MODE
import com.enecuum.androidapp.utils.KeyboardUtils
import org.greenrobot.eventbus.EventBus

@InjectViewState
class ReceiveSingleTabPresenter : MvpPresenter<ReceiveSingleTabView>() {
    fun handleArgs(arguments: Bundle?) {
        val sendMode = arguments?.getSerializable(RECEIVE_MODE) as SendReceiveMode
        val currency = when (sendMode) {
            SendReceiveMode.Enq -> Currency.Enq
            SendReceiveMode.EnqPlus -> Currency.EnqPlus
        }
        val totalAmount = PersistentStorage.getCurrencyAmount(currency)
        viewState.setupWithAmount(totalAmount)
    }

    fun onCopyClicked(text: String) {
        if(text.isEmpty())
            return
        val clipboard = EnecuumApplication.applicationContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("address", text)
        clipboard.primaryClip = clip
        EnecuumApplication.cicerone().router.showSystemMessage(EnecuumApplication.applicationContext().getString(R.string.text_copied))
    }

    fun onKeyboardVisibilityChanged(rootView: View) {
        EventBus.getDefault().post(KeyboardIsVisible(KeyboardUtils.isKeyboardShown(rootView)))
    }

}
