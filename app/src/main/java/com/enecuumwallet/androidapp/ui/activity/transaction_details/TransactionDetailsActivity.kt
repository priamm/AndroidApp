package com.enecuumwallet.androidapp.ui.activity.transaction_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.presenter.transaction_details.TransactionDetailsPresenter
import com.enecuumwallet.androidapp.presentation.view.BaseMiningView
import com.enecuumwallet.androidapp.presentation.view.transaction_details.TransactionDetailsView
import com.enecuumwallet.androidapp.ui.base_ui_primitives.FragmentNavigatorActivity
import com.enecuumwallet.androidapp.utils.MiningUtils
import kotlinx.android.synthetic.main.mining_toolbar.*


class TransactionDetailsActivity : FragmentNavigatorActivity(), TransactionDetailsView, BaseMiningView {

    override fun setupMiningPanel() {
        MiningUtils.refreshMiningPanel(miningIcon, miningStatus)
    }

    companion object {
        const val TAG = "TransactionDetailsActivity"
        const val TRANSACTION = "TRANSACTION"
        fun getIntent(context: Context): Intent = Intent(context, TransactionDetailsActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: TransactionDetailsPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)
        if(intent.extras != null)
            presenter.onCreate(intent.extras)
        setSupportActionBar(toolbar)
        MiningUtils.setupMiningPanel(miningStatusChanger, miningPanel, this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

}
