package com.enecuum.androidapp.presentation.view.create_seed

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.base_ui_primitives.FileOpener

interface CreateSeedView : FileOpener {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayRemainWords(size: Int)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonEnabled(enabled: Boolean)
}
