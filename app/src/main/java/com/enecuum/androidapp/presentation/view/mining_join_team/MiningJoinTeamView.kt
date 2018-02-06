package com.enecuum.androidapp.presentation.view.mining_join_team

import com.enecuum.androidapp.models.MiningHistoryItem
import com.enecuum.androidapp.presentation.view.HistoryView

interface MiningJoinTeamView : HistoryView<MiningHistoryItem> {
    fun setupWithMembersCount(teamMemberCount: Int)

}
