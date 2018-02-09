package com.enecuum.androidapp.presentation.view.receive_single_tab

import com.arellomobile.mvp.MvpView
import com.enecuum.androidapp.models.Transaction

interface ReceiveSingleTabView : MvpView {
    fun setupWithAmount(totalAmount: Float)
    fun setupWithTransaction(transaction: Transaction)
}
