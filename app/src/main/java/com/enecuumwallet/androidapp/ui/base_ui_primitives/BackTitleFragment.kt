package com.enecuumwallet.androidapp.ui.base_ui_primitives

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.ui.activity.main.MainActivity

/**
 * Created by oleg on 23.01.18.
 */
abstract class BackTitleFragment : OrdinalTitleFragment() {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            if(activity is MainActivity)
                EnecuumApplication.exitFromCurrentFragment()
        }
        return super.onOptionsItemSelected(item)
    }
}