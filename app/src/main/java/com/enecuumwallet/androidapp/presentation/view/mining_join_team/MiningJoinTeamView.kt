package com.enecuumwallet.androidapp.presentation.view.mining_join_team

import com.enecuumwallet.androidapp.models.MiningHistoryItem
import com.enecuumwallet.androidapp.presentation.view.HistoryView

interface MiningJoinTeamView : HistoryView<MiningHistoryItem> {
    fun setupWithMembersCount(teamMemberCount: Int)

}
