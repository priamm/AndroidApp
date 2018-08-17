package com.enecuum.androidapp.ui.fragment.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.inherited.models.MicroblockResponse
import com.enecuum.androidapp.presentation.presenter.balance.BalancePresenter
import com.enecuum.androidapp.presentation.view.balance.BalanceView
import com.enecuum.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuum.androidapp.utils.TransactionsHistoryRenderer
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

        start.setOnClickListener {
            presenter.onMiningToggle(null);
        }
        tokens.setOnClickListener({
            presenter.onTokensClick()
        })
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.turquoise_blue_three),
                android.graphics.PorterDuff.Mode.SRC_IN);

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

    override fun displayMicroblocks(count: Int) {
        minedText.text = "You has mined: $count  ENQ";
    }

    override fun displayKarma(karmaValue: Double) {
        karma.text = String.format(FORMAT, getString(R.string.karma), karmaValue)
    }

    override fun displayTeamSize(teamSize: Int) {
        minedText.post { minedText.text = "Joining, team size is: $teamSize"; }
    }

    override fun displayTransactionsHistory(transactionsList: List<MicroblockResponse>) {
        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun getTitle(): String {
        if (activity == null)
            return ""
        return activity!!.getString(R.string.my_wallet)
    }

    override fun setBalance(balance: Int?) {
        enqBalance.text = "ENQ " + balance?.toString();
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE

    }

    override fun hideProgress() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun changeButtonState(isStart: Boolean) {
        if (isStart) {
            start.text = getText(R.string.start_mining)
        } else {
            start.text = getText(R.string.stop_mining)
        }
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }
}
