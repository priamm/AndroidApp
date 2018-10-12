package com.enecuumwallet.androidapp.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.enecuumwallet.androidapp.R

/**
 * Created by oleg on 01.02.18.
 */
object SettingsInfoUtils {
    fun setupSettingInfo(
            titleView: TextView,
            icon: ImageView,
            description: TextView,
            callback: () -> Unit)
    {
        titleView.setOnClickListener {
            callback()
        }

        description.setOnClickListener {
            callback()
        }

        icon.setOnClickListener {
            if(description.visibility == View.GONE) {
                description.visibility = View.VISIBLE
                icon.setImageResource(R.drawable.expand_less)
            } else {
                description.visibility = View.GONE
                icon.setImageResource(R.drawable.info)
            }
        }
    }
}