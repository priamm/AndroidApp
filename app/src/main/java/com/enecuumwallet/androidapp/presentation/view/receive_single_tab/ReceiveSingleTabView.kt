package com.enecuumwallet.androidapp.presentation.view.receive_single_tab

import com.arellomobile.mvp.MvpView
import com.enecuumwallet.androidapp.models.Transaction

interface ReceiveSingleTabView : MvpView {
    fun setupWithAmount(totalAmount: Float)
    fun setupWithTransaction(transaction: Transaction)
}
