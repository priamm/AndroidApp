package com.enecuum.androidapp.utils

import android.view.View
import android.widget.ImageView
import com.enecuum.androidapp.R
import kotlinx.android.synthetic.main.fragment_new_account_pin.*

/**
 * Created by oleg on 26.01.18.
 */
object PinUtils {

    fun changePinState(pin1: ImageView, pin2: ImageView, pin3: ImageView, pin4: ImageView, currentLength : Int) {
        val isVisible = currentLength > 0
        setPinsVisibility(pin1, pin2, pin3, pin4, isVisible)
        if(isVisible) {
            changeDotState(currentLength, 0, pin1)
            changeDotState(currentLength, 1, pin2)
            changeDotState(currentLength, 2, pin3)
            changeDotState(currentLength, 3, pin4)
        }
    }

    private fun setPinsVisibility(pin1: ImageView, pin2: ImageView, pin3: ImageView, pin4: ImageView, isVisible: Boolean) {
        val visibility = if(isVisible) View.VISIBLE else View.INVISIBLE
        pin1.visibility = visibility
        pin2.visibility = visibility
        pin3.visibility = visibility
        pin4.visibility = visibility
    }

    private fun changeDotState(length: Int, value2check : Int, dot: ImageView) {
        if(length > value2check)
            dot.setImageResource(R.drawable.dot_1)
        else
            dot.setImageResource(R.drawable.dot_2)
    }
}