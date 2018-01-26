package com.enecuum.androidapp.presentation.view.create_seed

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CreateSeedView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayRemainWords(size: Int)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonEnabled(enabled: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun chooseSeedDirectory()
    @StateStrategyType(SkipStrategy::class)
    fun requestPermissions()

}
