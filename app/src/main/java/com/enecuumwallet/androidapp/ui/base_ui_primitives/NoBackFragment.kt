package com.enecuumwallet.androidapp.ui.base_ui_primitives

import android.support.v7.app.AppCompatActivity
import com.enecuumwallet.androidapp.ui.activity.main.MainActivity

/**
 * Created by oleg on 23.01.18.
 */
abstract class NoBackFragment : OrdinalTitleFragment() {
    override fun onResume() {
        super.onResume()
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        if(activity is MainActivity)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}