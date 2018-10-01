package com.enecuumwallet.androidapp.ui.base_ui_primitives.tab_fragments

import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.TabType

/**
 * Created by oleg on 30.01.18.
 */
class SendTabFragment : BaseTabFragment() {
    companion object {
        private var instance : SendTabFragment? = null
        fun singleton() : SendTabFragment {
            if(instance == null)
                instance = SendTabFragment()
            return instance!!
        }
    }

    override fun onResume() {
        super.onResume()
        if(!isSetupFinished) {
            isSetupFinished = true
            EnecuumApplication.navigateToFragment(FragmentType.SendOptions, TabType.Send)
        }
    }
}