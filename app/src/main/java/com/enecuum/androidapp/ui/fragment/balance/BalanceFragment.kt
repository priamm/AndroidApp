package com.enecuum.androidapp.ui.fragment.balance

import android.os.Bundle
import android.support.v4.widget.SlidingPaneLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.ui.base_ui_primitives.OrdinalTitleFragment
import com.enecuum.androidapp.presentation.presenter.balance.BalancePresenter
import com.enecuum.androidapp.presentation.view.balance.BalanceView
import com.enecuum.androidapp.ui.adapters.TransactionsListAdapter
import com.enecuum.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuum.androidapp.utils.TransactionsHistoryRenderer
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_balance.*

class BalanceFragment : NoBackFragment(), BalanceView {

    companion object {
        const val FORMAT = "%s %.8f"
        const val TAG = "BalanceFragment"

        fun newInstance(): BalanceFragment {
            val fragment: BalanceFragment = BalanceFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: BalancePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tokens.setOnClickListener({
            presenter.onTokensClick()
        })
        presenter.onCreate()
        setHasOptionsMenu(true)
        TransactionsHistoryRenderer.configurePanelListener(slidingLayout, panelHint)
    }

    override fun displayCurrencyRates(enq2Usd: Double, enq2Btc: Double) {
        enqBtc.text = String.format("%f ENQ/BTC", enq2Btc)
        enqUsd.text = String.format("%f ENQ/USD", enq2Usd)
    }

    override fun displayBalances(enq: Double, enqPlus: Double) {
        enqBalance.text = String.format("ENQ %.8f", enq)
        enqPlusBalance.text = String.format("ENQ+ %.8f", enqPlus)
    }

    override fun displayPoints(pointsValue: Double) {
        points.text = String.format(FORMAT, getString(R.string.points), pointsValue)
    }

    override fun displayKarma(karmaValue: Double) {
        karma.text = String.format(FORMAT, getString(R.string.karma), karmaValue)
    }

    override fun displayTransactionsHistory(transactionsList: List<Transaction>) {
        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun getTitle(): String {
        if(activity == null)
            return ""
        return activity!!.getString(R.string.my_wallet)
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }
}
