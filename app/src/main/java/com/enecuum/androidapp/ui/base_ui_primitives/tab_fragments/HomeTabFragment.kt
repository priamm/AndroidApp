package com.enecuum.androidapp.ui.base_ui_primitives.tab_fragments

import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentType

/**
 * Created by oleg on 30.01.18.
 */
class HomeTabFragment : BaseTabFragment() {
    companion object {
        private var instance : HomeTabFragment? = null
        fun singleton() : HomeTabFragment {
            if(instance == null)
                instance = HomeTabFragment()
            return instance!!
        }
    }

    override fun onResume() {
        super.onResume()
        if(!isSetupFinished) {
            isSetupFinished = true
            EnecuumApplication.navigateToFragment(FragmentType.Balance)
        }
    }
}