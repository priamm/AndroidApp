package com.enecuum.androidapp.ui.activity.mining

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.navigation.FragmentNavigator
import com.enecuum.androidapp.presentation.presenter.mining.MiningPresenter
import com.enecuum.androidapp.presentation.view.mining.MiningView
import com.enecuum.androidapp.ui.base_ui_primitives.BackActivity
import kotlinx.android.synthetic.main.activity_mining.*


class MiningActivity : BackActivity(), MiningView {
    companion object {
        const val TAG = "MiningActivity"
        fun getIntent(context: Context): Intent = Intent(context, MiningActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: MiningPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mining)
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        EnecuumApplication.fragmentCicerone().navigatorHolder.setNavigator(FragmentNavigator(this, supportFragmentManager, R.id.container))
    }

    override fun onPause() {
        super.onPause()
        EnecuumApplication.fragmentCicerone().navigatorHolder.removeNavigator()
    }
}
