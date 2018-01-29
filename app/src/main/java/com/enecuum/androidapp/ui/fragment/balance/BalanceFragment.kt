package com.enecuum.androidapp.ui.fragment.balance

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.base_ui_primitives.OrdinalTitleFragment
import com.enecuum.androidapp.presentation.presenter.balance.BalancePresenter
import com.enecuum.androidapp.presentation.view.balance.BalanceView
import kotlinx.android.synthetic.main.fragment_balance.*

class BalanceFragment : OrdinalTitleFragment(), BalanceView {
    override fun getTitle(): String {
        if(activity == null)
            return ""
        return activity!!.getString(R.string.my_wallet)
    }

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
        points.text = resources.getQuantityString(R.plurals.point_plural, pointsValue.toInt(), pointsValue)
    }

    override fun displayKarma(karmaValue: Double) {
        karma.text = String.format(FORMAT, getString(R.string.karma), karmaValue)
    }

    override fun displayPercentage(percentage: Double, karmaPercentage: Double) {
        percent.text = resources.getQuantityString(R.plurals.percent_plural, percentage.toInt(), percentage)
        karmaPercent.text = String.format(FORMAT, getString(R.string.karma_percent), karmaPercentage)
    }

    override fun onResume() {
        super.onResume()
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
