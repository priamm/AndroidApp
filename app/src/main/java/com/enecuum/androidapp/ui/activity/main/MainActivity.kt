package com.enecuum.androidapp.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentNavigator
import com.enecuum.androidapp.presentation.presenter.main.MainPresenter
import com.enecuum.androidapp.presentation.view.main.MainView

class MainActivity : MvpAppCompatActivity(), MainView {
    companion object {
        const val TAG = "MainActivity"
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private var isSetupFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        EnecuumApplication.cicerone().navigatorHolder.setNavigator(FragmentNavigator(this, supportFragmentManager, R.id.container))
        if(!isSetupFinished) {
            isSetupFinished = true
            presenter.finishSetup()
        }
    }

    override fun onPause() {
        super.onPause()
        EnecuumApplication.cicerone().navigatorHolder.removeNavigator()
    }
}
