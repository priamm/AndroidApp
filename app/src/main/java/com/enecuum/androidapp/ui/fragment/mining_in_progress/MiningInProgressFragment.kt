package com.enecuum.androidapp.ui.fragment.mining_in_progress

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.MiningHistoryItem
import com.enecuum.androidapp.models.PoaMemberStatus
import com.enecuum.androidapp.presentation.presenter.mining_in_progress.MiningInProgressPresenter
import com.enecuum.androidapp.presentation.view.mining_in_progress.MiningInProgressView
import com.enecuum.androidapp.ui.activity.testActivity.PoaService
import com.enecuum.androidapp.utils.TransactionsHistoryRenderer
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_mining_in_progress.*

class MiningInProgressFragment : MvpAppCompatFragment(), MiningInProgressView {
    companion object {
        const val TAG = "MiningInProgressFragment"

        fun newInstance(): MiningInProgressFragment {
            val fragment: MiningInProgressFragment = MiningInProgressFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: MiningInProgressPresenter
    private val graphData = LineGraphSeries<DataPoint>()

    private var poaService: PoaService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mining_in_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        poaService = PoaService(view.context)
        btConnect.setOnClickListener {
            poaService?.connectAs(nodeNum.text.toString().toInt())
        }

        btStartEvent.setOnClickListener {
            poaService?.startEvent()
        }

        presenter.onCreate()
        setupGraph()
        setHasOptionsMenu(true)
        TransactionsHistoryRenderer.configurePanelListener(slidingLayout, panelHint)
        val supportActivity = activity as AppCompatActivity?
        supportActivity?.supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.color.navy))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.mining_menu, menu)
        val stop = menu?.findItem(R.id.stop)
        stop?.actionView?.setOnClickListener {
            presenter.onStopClick()
        }
    }

    private fun setupGraph() {
        graph.gridLabelRenderer.gridColor = ContextCompat.getColor(activity!!, R.color.french_blue)
        graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.BOTH
        graph.viewport.backgroundColor = ContextCompat.getColor(activity!!, R.color.dark_indigo_three)
        graph.viewport.borderColor = ContextCompat.getColor(activity!!, android.R.color.white)
        graph.viewport.isScrollable = true
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(40.0)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(0.0)
        graph.viewport.setMaxY(100.0)
        graph.legendRenderer.isVisible = false
        graph.gridLabelRenderer.isVerticalLabelsVisible = false
        graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        graphData.color = ContextCompat.getColor(activity!!, R.color.dark_sky_blue)
        graphData.isDrawDataPoints = true
        graphData.dataPointsRadius = 9f
        graphData.thickness = 3
        graphData.isDrawBackground = true
        graphData.backgroundColor = ContextCompat.getColor(activity!!, R.color.turquoise_blue_five)
        graph.addSeries(graphData)
    }

    override fun displayTransactionsHistory(transactionsList: List<MiningHistoryItem>) {
        TransactionsHistoryRenderer.displayMiningHistoryInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun setupWithStatus(status: PoaMemberStatus) {
        when (status) {
            PoaMemberStatus.PoaMember -> {
                memberStatus.text = getString(R.string.poa_member)
                statusIcon.setImageResource(R.drawable.member)
            }
            PoaMemberStatus.TeamLead -> {
                memberStatus.text = getString(R.string.team_lead)
                statusIcon.setImageResource(R.drawable.team_lead)
            }
            PoaMemberStatus.Verificator -> {
                memberStatus.text = getString(R.string.verificator)
                statusIcon.setImageResource(R.drawable.verificator)
            }
        }
    }

    override fun displayHashRate(hashRate: Int) {
        hashRateText.text = String.format(getString(R.string.hash_rate), hashRate)
    }

    override fun displayTotalBalance(totalBalance: Int) {
        totalBalanceText.text = String.format(getString(R.string.total_balance), totalBalance)
    }

    override fun refreshGraph(newGraphData: Array<DataPoint>) {
        graphData.resetData(newGraphData)
    }
}
