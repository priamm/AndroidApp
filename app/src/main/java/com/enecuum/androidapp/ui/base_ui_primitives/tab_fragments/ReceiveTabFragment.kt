package com.enecuum.androidapp.ui.base_ui_primitives.tab_fragments

import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType

/**
 * Created by oleg on 30.01.18.
 */
class ReceiveTabFragment : BaseTabFragment() {
    companion object {
        private var instance : ReceiveTabFragment? = null
        fun singleton() : ReceiveTabFragment {
            if(instance == null)
                instance = ReceiveTabFragment()
            return instance!!
        }
    }

    override fun onResume() {
        super.onResume()
        if(!isSetupFinished) {
            isSetupFinished = true
            EnecuumApplication.navigateToFragment(FragmentType.ReceiveByAddress, TabType.Receive)
        }
    }
}