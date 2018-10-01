package com.enecuumwallet.androidapp.ui.base_ui_primitives


import android.os.Bundle
import android.os.PersistableBundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.ActivityNavigator

/**
 * Created by oleg on 22.01.18.
 */
open class BaseActivity : MvpAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
    }

    override fun onResume() {
        super.onResume()
        EnecuumApplication.cicerone().navigatorHolder.setNavigator(ActivityNavigator(this))
    }

    override fun onPause() {
        super.onPause()
        EnecuumApplication.cicerone().navigatorHolder.removeNavigator()
    }
}