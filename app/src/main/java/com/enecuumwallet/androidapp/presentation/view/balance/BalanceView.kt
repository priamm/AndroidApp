package com.enecuumwallet.androidapp.presentation.view.balance

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuumwallet.androidapp.presentation.view.HistoryView

interface BalanceView : HistoryView<String> {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayCurrencyRates(enq2Usd: Double, enq2Btc: Double)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayBalances(enq: Double, enqPlus: Double)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayTeamSize(teamSize: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateProgressMessage(str:String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun changeButtonState(isStart:Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setBalance(balance: Int?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showConnectionError(message: String)
}
