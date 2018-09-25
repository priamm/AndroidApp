package com.enecuumwallet.androidapp.presentation.view.receive_qr

import com.arellomobile.mvp.MvpView

interface ReceiveQrView : MvpView {
    fun setupWithAddress(address: String)
}
