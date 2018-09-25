package com.enecuumwallet.androidapp.ui.activity.change_pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.presenter.change_pin.ChangePinPresenter
import com.enecuumwallet.androidapp.presentation.view.change_pin.ChangePinView
import com.enecuumwallet.androidapp.ui.adapters.ChangePinPagerAdapter
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackActivity
import com.enecuumwallet.androidapp.utils.MiningUtils
import kotlinx.android.synthetic.main.activity_change_pin.*
import kotlinx.android.synthetic.main.mining_toolbar.*


class ChangePinActivity : BackActivity(), ChangePinView {
    companion object {
        const val TAG = "ChangePinActivity"
        fun getIntent(context: Context): Intent = Intent(context, ChangePinActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: ChangePinPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val adapter = ChangePinPagerAdapter(supportFragmentManager)
        pager.adapter = adapter
        indicator.setViewPager(pager)
        next.setOnClickListener {
            presenter.onNextClick(pager.currentItem)
        }
        MiningUtils.setupMiningPanel(miningStatusChanger, miningPanel, this)
        presenter.onCreate()
    }

    override fun changeButtonState(enable: Boolean) {
        next.isEnabled = enable
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun setupMiningPanel() {
        MiningUtils.refreshMiningPanel(miningIcon, miningStatus)
    }

    override fun onResume() {
        super.onResume()
        setupMiningPanel()
    }

    override fun moveNext() {
        pager.setCurrentItem(pager.currentItem+1, true)
    }
}
