package com.enecuum.androidapp.presentation.presenter.mining_in_progress

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.models.MiningHistoryItem
import com.enecuum.androidapp.models.PoaMemberStatus
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.presentation.view.mining_in_progress.MiningInProgressView
import com.jjoe64.graphview.series.DataPoint
import java.util.*

@InjectViewState
class MiningInProgressPresenter : MvpPresenter<MiningInProgressView>() {
    private var maxX = 0.0

    fun onCreate() {
        val transactionsList = listOf(
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0),
                MiningHistoryItem(1517307367,1517307367, 10.0)
        )
        viewState.displayTransactionsHistory(transactionsList)
        //TODO: obtain member status from network
        val random = Random(Date().time)
        val isTeamLead = random.nextInt()%2 == 0
        val status = if(isTeamLead) PoaMemberStatus.TeamLead else PoaMemberStatus.PoaMember
        viewState.setupWithStatus(status)
        //TODO: get hashrate from mining algorithm
        val hashRate = random.nextInt(10)
        viewState.displayHashRate(hashRate)
        val totalBalance = 5000
        viewState.displayTotalBalance(totalBalance)
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewState.refreshGraph(getNewGraphData())
            }
        }, 0, 300)
    }

    private fun getNewGraphData() : Array<DataPoint> {
        val count = 40
        maxX = 0.0
        val random = Random(Date().time)
        val values = Array<DataPoint>(count, {
            val v = DataPoint(maxX, 5000.0*random.nextDouble())
            maxX += 1.0
            return@Array v
        })
        return values
    }

}
