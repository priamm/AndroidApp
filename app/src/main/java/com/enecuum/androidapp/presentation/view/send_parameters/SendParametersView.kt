package com.enecuum.androidapp.presentation.view.send_parameters

import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.presentation.view.ButtonStateView
import com.enecuum.androidapp.presentation.view.HistoryView
import com.enecuum.androidapp.presentation.view.TransactionsView

interface SendParametersView : HistoryView<Transaction>, ButtonStateView, TransactionsView
