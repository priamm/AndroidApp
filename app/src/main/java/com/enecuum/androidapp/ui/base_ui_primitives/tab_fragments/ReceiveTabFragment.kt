package com.enecuum.androidapp.ui.base_ui_primitives.tab_fragments

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
}