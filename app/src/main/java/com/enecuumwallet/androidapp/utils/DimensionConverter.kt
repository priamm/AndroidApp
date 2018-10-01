package com.enecuumwallet.androidapp.utils

import android.util.TypedValue
import com.enecuumwallet.androidapp.application.EnecuumApplication


/**
 * Created by oleg on 25.01.18.
 */
object DimensionConverter {
    fun dipToPixels(dipValue: Float): Float {
        val metrics = EnecuumApplication.applicationContext().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}