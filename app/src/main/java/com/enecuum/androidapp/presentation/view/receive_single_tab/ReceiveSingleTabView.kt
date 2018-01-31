package com.enecuum.androidapp.presentation.view.receive_single_tab

import com.arellomobile.mvp.MvpView

interface ReceiveSingleTabView : MvpView {
    fun setupWithAmount(totalAmount: Float)
}
