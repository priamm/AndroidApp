package com.enecuum.androidapp.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.models.Transaction

/**
 * Created by oleg on 31.01.18.
 */
interface TransactionsHistoryView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayTransactionsHistory(transactionsList: List<Transaction>)
}