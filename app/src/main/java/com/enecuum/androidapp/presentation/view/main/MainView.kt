package com.enecuum.androidapp.presentation.view.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface MainView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupMiningPanel(miningInProgress: Boolean)

}
