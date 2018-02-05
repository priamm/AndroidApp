package com.enecuum.androidapp.presentation.view.send_single_tab

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SendSingleTabView : MvpView {
    fun setupWithAmount(amount: Float)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun changeAddress(newValue: String)
}
