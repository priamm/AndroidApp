package com.enecuum.androidapp.base_ui_primitives

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.MvpAppCompatFragment

/**
 * Created by oleg on 23.01.18.
 */
abstract class OrdinalTitleFragment : TitleFragment() {
    override fun onResume() {
        super.onResume()
        activity?.title = getTitle()
    }
}