package com.enecuum.androidapp.presentation.view.balance

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.models.Transaction

interface BalanceView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayCurrencyRates(enq2Usd: Double, enq2Btc: Double)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayBalances(enq: Double, enqPlus: Double)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayPoints(pointsValue: Double)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayKarma(karmaValue: Double)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayPercentage(percentage: Double, karmaPercentage: Double)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayTransactionsHistory(transactionsList: List<Transaction>)
}
