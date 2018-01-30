package com.enecuum.androidapp.base_ui_primitives.tab_fragments

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
}