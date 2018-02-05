package com.enecuum.androidapp.presentation.view.mining_join_team

import com.enecuum.androidapp.presentation.view.TransactionsHistoryView

interface MiningJoinTeamView : TransactionsHistoryView {
    fun setupWithMembersCount(teamMemberCount: Int)

}
