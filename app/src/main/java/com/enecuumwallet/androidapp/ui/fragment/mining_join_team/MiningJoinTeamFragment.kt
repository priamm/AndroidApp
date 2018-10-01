package com.enecuumwallet.androidapp.ui.fragment.mining_join_team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.view.mining_join_team.MiningJoinTeamView
import com.enecuumwallet.androidapp.presentation.presenter.mining_join_team.MiningJoinTeamPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.models.MiningHistoryItem
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.utils.TransactionsHistoryRenderer
import kotlinx.android.synthetic.main.fragment_mining_join_team.*

class MiningJoinTeamFragment : MvpAppCompatFragment(), MiningJoinTeamView {
    companion object {
        const val TAG = "MiningJoinTeamFragment"

        fun newInstance(bundle: Bundle): MiningJoinTeamFragment {
            val fragment: MiningJoinTeamFragment = MiningJoinTeamFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: MiningJoinTeamPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mining_join_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate(arguments)
        join.setOnClickListener {
            presenter.onJoinClick()
        }
        TransactionsHistoryRenderer.configurePanelListener(slidingLayout, panelHint)
    }

    override fun displayTransactionsHistory(transactionsList: List<MiningHistoryItem>) {
        TransactionsHistoryRenderer.displayMiningHistoryInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun setupWithMembersCount(teamMemberCount: Int) {
        val text = String.format(resources.getQuantityString(R.plurals.team_members, teamMemberCount), teamMemberCount)
        team.text = text
    }
}
