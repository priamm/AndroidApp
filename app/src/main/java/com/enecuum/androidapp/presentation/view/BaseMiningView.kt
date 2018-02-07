package com.enecuum.androidapp.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by oleg on 31.01.18.
 */
interface BaseMiningView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupMiningPanel()
}