package com.enecuum.androidapp.presentation.view.send_finish

import com.arellomobile.mvp.MvpView
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.presentation.view.TransactionsHistoryView

interface SendFinishView : TransactionsHistoryView {
    fun setupWithData(address: String?, amount: Float?, currency: Currency)
}
