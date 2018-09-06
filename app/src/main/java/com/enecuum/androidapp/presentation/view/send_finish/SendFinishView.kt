package com.enecuum.androidapp.presentation.view.send_finish

import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.presentation.view.HistoryView

interface SendFinishView : HistoryView<Transaction> {
    fun setupWithData(address: String?, amount: Float?, currency: Currency)
    fun hideHistory()
    fun doOnStartSending()
    fun doOnResult(isSuccess: Boolean)
}
