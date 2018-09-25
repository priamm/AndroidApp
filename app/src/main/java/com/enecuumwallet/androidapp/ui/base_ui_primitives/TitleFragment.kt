package com.enecuumwallet.androidapp.ui.base_ui_primitives

import android.view.Menu
import android.view.MenuInflater
import com.arellomobile.mvp.MvpAppCompatFragment

/**
 * Created by oleg on 23.01.18.
 */
abstract class TitleFragment : MvpAppCompatFragment() {
    abstract fun getTitle() : String
    protected var menu: Menu? = null
    protected var menuInflater: MenuInflater? = null

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        this.menuInflater = inflater
    }
}