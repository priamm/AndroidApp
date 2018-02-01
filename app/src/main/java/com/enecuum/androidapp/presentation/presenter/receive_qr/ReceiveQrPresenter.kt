package com.enecuum.androidapp.presentation.presenter.receive_qr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.presentation.view.receive_qr.ReceiveQrView
import com.enecuum.androidapp.ui.fragment.receive_qr.ReceiveQrFragment.Companion.ADDRESS

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
        val clipboard = EnecuumApplication.applicationContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("address", address!!)
        clipboard.primaryClip = clip
        EnecuumApplication.cicerone().router.showSystemMessage(EnecuumApplication.applicationContext().getString(R.string.text_copied))
    }

}
