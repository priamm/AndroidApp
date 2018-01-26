package com.enecuum.androidapp.presentation.view.restore_pin

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface RestorePinView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonEnabled(enabled: Boolean)

}
