package com.enecuumwallet.androidapp.presentation.view.receive_by_address

import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.presentation.view.ButtonStateView
import com.enecuumwallet.androidapp.presentation.view.HistoryView
import com.enecuumwallet.androidapp.presentation.view.TransactionsView

interface ReceiveByAddressView : HistoryView<Transaction>, TransactionsView, ButtonStateView
