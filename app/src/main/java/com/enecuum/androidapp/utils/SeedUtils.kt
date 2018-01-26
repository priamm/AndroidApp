package com.enecuum.androidapp.utils

import android.view.View
import android.widget.TextView
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication

/**
 * Created by oleg on 26.01.18.
 */
object SeedUtils {
    fun displayRemainingCount(size : Int, hintText : TextView) {
        if(size <= 0)
            hintText.visibility = View.INVISIBLE
        else
            hintText.visibility = View.VISIBLE
        hintText.text = String.format("%s %d", hintText.context.getString(R.string.words_left), size)
    }
}