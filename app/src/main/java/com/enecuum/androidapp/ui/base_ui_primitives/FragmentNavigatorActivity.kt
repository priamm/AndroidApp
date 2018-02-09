package com.enecuum.androidapp.ui.base_ui_primitives

import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentNavigator

/**
 * Created by oleg on 09.02.18.
 */
open class FragmentNavigatorActivity : BackActivity() {
    override fun onResume() {
        super.onResume()
        EnecuumApplication.fragmentCicerone().navigatorHolder.setNavigator(FragmentNavigator(this, supportFragmentManager, R.id.container))
    }

    override fun onPause() {
        super.onPause()
        EnecuumApplication.fragmentCicerone().navigatorHolder.removeNavigator()
    }
}