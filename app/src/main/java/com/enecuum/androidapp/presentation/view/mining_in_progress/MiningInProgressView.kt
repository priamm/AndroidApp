package com.enecuum.androidapp.presentation.view.mining_in_progress

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.models.PoaMemberStatus
import com.enecuum.androidapp.presentation.view.TransactionsHistoryView
import com.jjoe64.graphview.series.DataPoint

interface MiningInProgressView : TransactionsHistoryView {
    fun setupWithStatus(status: PoaMemberStatus)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayHashRate(hashRate: Int)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayTotalBalance(totalBalance: Int)
    fun refreshGraph(newGraphData: Array<DataPoint>)
}
