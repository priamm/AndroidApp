package com.enecuum.androidapp.presentation.view.receive_by_address

import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.presentation.view.ButtonStateView
import com.enecuum.androidapp.presentation.view.HistoryView
import com.enecuum.androidapp.presentation.view.TransactionsView

interface ReceiveByAddressView : HistoryView<Transaction>, TransactionsView, ButtonStateView
