package com.enecuum.androidapp.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.events.MainActivityStopped
import com.enecuum.androidapp.navigation.TabsNavigator
import com.enecuum.androidapp.presentation.presenter.main.MainPresenter
import com.enecuum.androidapp.presentation.view.main.MainView
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus

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
        bottomNavigation.enableAnimation(false)
        bottomNavigation.enableItemShiftingMode(false)
        bottomNavigation.enableShiftingMode(false)
        bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.actionHome -> presenter.onHomeClicked()
                    R.id.actionSend -> presenter.onSendClicked()
                    R.id.actionReceive -> presenter.onReceiveClicked()
                    R.id.actionSettings -> presenter.onSettingsClicked()
                }
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        EnecuumApplication.tabCicerone().navigatorHolder.setNavigator(TabsNavigator(this, supportFragmentManager, R.id.container))
        if(!isSetupFinished) {
            isSetupFinished = true
            presenter.finishSetup()
        }
    }

    override fun onPause() {
        super.onPause()
        EnecuumApplication.tabCicerone().navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        val backStackCount = EnecuumApplication.getCurrentBackStackCount()
        if(backStackCount <= 1) {
            EventBus.getDefault().post(MainActivityStopped())
            finish()
            return
        }
        EnecuumApplication.exitFromCurrentFragment()
    }
}
