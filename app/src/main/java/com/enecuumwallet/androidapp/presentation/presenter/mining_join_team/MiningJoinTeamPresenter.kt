package com.enecuumwallet.androidapp.presentation.presenter.mining_join_team

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.models.MiningHistoryItem
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.presentation.presenter.mining_loading.MiningLoadingPresenter
import com.enecuumwallet.androidapp.presentation.view.mining_join_team.MiningJoinTeamView

@InjectViewState
class MiningJoinTeamPresenter : MvpPresenter<MiningJoinTeamView>() {
    fun onCreate(arguments: Bundle?) {
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
        if(arguments != null) {
            val teamMemberCount = arguments.getInt(MiningLoadingPresenter.TEAM_COUNT)
            viewState.setupWithMembersCount(teamMemberCount)
        }
    }

    fun onJoinClick() {
        EnecuumApplication.fragmentCicerone().router.replaceScreen(FragmentType.MiningProgress.toString())
    }

}
