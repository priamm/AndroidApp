package com.enecuum.androidapp.ui.base_ui_primitives

import android.support.v7.app.AppCompatActivity

/**
 * Created by oleg on 23.01.18.
 */
abstract class NoBackFragment : OrdinalTitleFragment() {
    override fun onResume() {
        super.onResume()
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}