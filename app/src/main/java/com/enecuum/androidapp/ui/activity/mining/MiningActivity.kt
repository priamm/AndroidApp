package com.enecuum.androidapp.ui.activity.mining

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentNavigator
import com.enecuum.androidapp.presentation.presenter.mining.MiningPresenter
import com.enecuum.androidapp.presentation.view.mining.MiningView
import com.enecuum.androidapp.ui.base_ui_primitives.BackActivity
import com.enecuum.androidapp.ui.base_ui_primitives.FragmentNavigatorActivity
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
