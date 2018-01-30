package com.enecuum.androidapp.ui.base_ui_primitives

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by oleg on 26.01.18.
 */
interface FileOpener: MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun chooseDirectory()
    @StateStrategyType(SkipStrategy::class)
    fun requestPermissions()
}