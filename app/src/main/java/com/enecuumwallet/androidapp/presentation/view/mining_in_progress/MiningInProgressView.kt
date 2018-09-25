package com.enecuumwallet.androidapp.presentation.view.mining_in_progress

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuumwallet.androidapp.models.MiningHistoryItem
import com.enecuumwallet.androidapp.models.PoaMemberStatus
import com.enecuumwallet.androidapp.presentation.view.HistoryView
import com.jjoe64.graphview.series.DataPoint

interface MiningInProgressView : HistoryView<MiningHistoryItem> {
    fun setupWithStatus(status: PoaMemberStatus)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayHashRate(hashRate: Int)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayTotalBalance(totalBalance: Int)
    fun refreshGraph(newGraphData: Array<DataPoint>)
}
