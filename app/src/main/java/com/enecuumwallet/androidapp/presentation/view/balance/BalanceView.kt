package com.enecuumwallet.androidapp.presentation.view.balance

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuumwallet.androidapp.presentation.view.HistoryView

interface BalanceView : HistoryView<String> {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun displayCurrencyRates(enq2Usd: Double, enq2Btc: Double)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun displayBalances(enq: Double, enqPlus: Double)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun displayTeamSize(teamSize: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateProgressMessage(str:String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changeButtonState(isStart:Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setBalance(balance: Int?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showLoading()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideLoading()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showConnectionError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateMiningStatus(status : String)
}
