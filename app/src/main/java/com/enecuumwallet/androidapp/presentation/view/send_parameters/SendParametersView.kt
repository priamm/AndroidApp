package com.enecuumwallet.androidapp.presentation.view.send_parameters

import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.presentation.view.ButtonStateView
import com.enecuumwallet.androidapp.presentation.view.HistoryView
import com.enecuumwallet.androidapp.presentation.view.TransactionsView

interface SendParametersView : HistoryView<Transaction>, ButtonStateView, TransactionsView
