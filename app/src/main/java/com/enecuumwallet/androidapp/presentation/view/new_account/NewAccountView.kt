package com.enecuumwallet.androidapp.presentation.view.new_account

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuumwallet.androidapp.presentation.view.ButtonStateView
import com.enecuumwallet.androidapp.presentation.view.PagerView

interface NewAccountView : ButtonStateView, PagerView {
    @StateStrategyType(SkipStrategy::class)
    fun displaySkipDialog()
}
