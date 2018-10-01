package com.enecuumwallet.androidapp.ui.base_ui_primitives

/**
 * Created by oleg on 23.01.18.
 */
abstract class OrdinalTitleFragment : TitleFragment() {
    override fun onResume() {
        super.onResume()
        activity?.title = getTitle()
    }
}