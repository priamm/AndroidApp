package com.enecuumwallet.androidapp.presentation.view.send_finish

import com.enecuumwallet.androidapp.models.Currency
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.presentation.view.HistoryView

interface SendFinishView : HistoryView<Transaction> {
    fun setupWithData(address: String?, amount: Int?, currency: Currency)
    fun hideHistory()
    fun doOnStartSending()
    fun doOnResult(isSuccess: Boolean)
}
