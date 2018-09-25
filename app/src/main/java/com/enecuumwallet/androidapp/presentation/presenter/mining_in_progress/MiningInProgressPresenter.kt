package com.enecuumwallet.androidapp.presentation.presenter.mining_in_progress

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.models.MiningHistoryItem
import com.enecuumwallet.androidapp.models.PoaMemberStatus
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.models.TransactionType
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.mining_in_progress.MiningInProgressView
import com.jjoe64.graphview.series.DataPoint
import java.util.*

@InjectViewState
class MiningInProgressPresenter : MvpPresenter<MiningInProgressView>() {
    private var maxX = 0.0

    fun onCreate() {
        PersistentStorage.setMiningInProgress(true)
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
        val variable = random.nextInt()%3
        val status = when(variable) {
            0 -> PoaMemberStatus.TeamLead
            1 -> PoaMemberStatus.Verificator
            2 -> PoaMemberStatus.PoaMember
            else -> {
                PoaMemberStatus.PoaMember
            }
        }
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
        val count = 41
        maxX = 0.0
        val random = Random(Date().time)
        val values = Array<DataPoint>(count, {
            val v = DataPoint(maxX, 50.0*random.nextDouble())
            maxX += 1.0
            return@Array v
        })
        return values
    }

    fun onStopClick() {
        PersistentStorage.setMiningInProgress(false)
        EnecuumApplication.fragmentCicerone().router.replaceScreen(FragmentType.MiningStart.toString())
    }

}
