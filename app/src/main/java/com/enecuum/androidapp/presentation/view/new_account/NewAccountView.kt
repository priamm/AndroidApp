package com.enecuum.androidapp.presentation.view.new_account

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.presentation.view.ButtonStateView

interface NewAccountView : ButtonStateView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openNextScreen()
    @StateStrategyType(SkipStrategy::class)
    fun displaySkipDialog()
}
