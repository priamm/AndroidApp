package com.enecuum.androidapp.ui.base_ui_primitives

import com.arellomobile.mvp.MvpAppCompatFragment

/**
 * Created by oleg on 23.01.18.
 */
abstract class TitleFragment : MvpAppCompatFragment() {
    abstract fun getTitle() : String
}