package com.enecuum.androidapp.presentation.view.forgot

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ForgotPinView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonEnabled(enabled: Boolean)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayRemainWords(size: Int)
}
