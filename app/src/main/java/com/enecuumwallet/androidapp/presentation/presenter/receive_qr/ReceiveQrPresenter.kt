package com.enecuumwallet.androidapp.presentation.presenter.receive_qr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.presentation.view.receive_qr.ReceiveQrView
import com.enecuumwallet.androidapp.ui.fragment.receive_qr.ReceiveQrFragment.Companion.ADDRESS
import com.enecuumwallet.androidapp.utils.KeyboardUtils

@InjectViewState
class ReceiveQrPresenter : MvpPresenter<ReceiveQrView>() {
    private var address: String? = null

    fun handleArgs(arguments: Bundle?) {
        if(arguments != null) {
            address = arguments.getString(ADDRESS)
            if(address != null)
                viewState.setupWithAddress(address!!)
            else
                EnecuumApplication.exitFromCurrentFragment()
        }
    }

    fun onCopyClick() {
        KeyboardUtils.copyToClipboard(address!!)
        EnecuumApplication.cicerone().router.showSystemMessage(EnecuumApplication.applicationContext().getString(R.string.text_copied))
    }

}
