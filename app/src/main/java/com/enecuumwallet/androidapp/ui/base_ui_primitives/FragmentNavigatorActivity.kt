package com.enecuumwallet.androidapp.ui.base_ui_primitives

import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentNavigator

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