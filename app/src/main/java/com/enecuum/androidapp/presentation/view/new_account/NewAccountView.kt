package com.enecuum.androidapp.presentation.view.new_account

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface NewAccountView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openNextScreen()
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun changeButtonState(enable: Boolean)
}
