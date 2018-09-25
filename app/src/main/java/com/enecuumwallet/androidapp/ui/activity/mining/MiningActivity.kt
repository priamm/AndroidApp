package com.enecuumwallet.androidapp.ui.activity.mining

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentNavigator
import com.enecuumwallet.androidapp.presentation.presenter.mining.MiningPresenter
import com.enecuumwallet.androidapp.presentation.view.mining.MiningView
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackActivity
import com.enecuumwallet.androidapp.ui.base_ui_primitives.FragmentNavigatorActivity
import kotlinx.android.synthetic.main.transparent_toolbar.*


class MiningActivity : FragmentNavigatorActivity(), MiningView {
    companion object {
        const val TAG = "MiningActivity"
        fun getIntent(context: Context): Intent = Intent(context, MiningActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: MiningPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mining)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.onCreate()
    }
}
