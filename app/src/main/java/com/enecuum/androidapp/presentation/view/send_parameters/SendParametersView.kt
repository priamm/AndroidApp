package com.enecuum.androidapp.presentation.view.send_parameters

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.presentation.view.ButtonStateView
import com.enecuum.androidapp.presentation.view.KeyboardVisibilityView
import com.enecuum.androidapp.presentation.view.TransactionsHistoryView

interface SendParametersView : TransactionsHistoryView, ButtonStateView, KeyboardVisibilityView
