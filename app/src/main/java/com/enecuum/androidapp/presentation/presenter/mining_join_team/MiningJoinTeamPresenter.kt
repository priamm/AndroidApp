package com.enecuum.androidapp.presentation.presenter.mining_join_team

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.presentation.presenter.mining_loading.MiningLoadingPresenter
import com.enecuum.androidapp.presentation.view.mining_join_team.MiningJoinTeamView
import com.enecuum.androidapp.ui.fragment.mining_loading.MiningLoadingFragment

@InjectViewState
class MiningJoinTeamPresenter : MvpPresenter<MiningJoinTeamView>() {
    fun onCreate(arguments: Bundle?) {
        val transactionsList = listOf(
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…")
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
