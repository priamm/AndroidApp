package com.enecuum.androidapp.base_ui_primitives.tab_fragments

/**
 * Created by oleg on 30.01.18.
 */
class SettingsTabFragment : BaseTabFragment() {
    companion object {
        private var instance : SettingsTabFragment? = null
        fun singleton() : SettingsTabFragment {
            if(instance == null)
                instance = SettingsTabFragment()
            return instance!!
        }
    }
}