package com.enecuum.androidapp.presentation.view.change_pin

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.presentation.view.BaseMiningView
import com.enecuum.androidapp.presentation.view.ButtonStateView
import com.enecuum.androidapp.presentation.view.PinView
import com.enecuum.androidapp.ui.activity.change_pin.ChangePinActivity

interface ChangePinView : ButtonStateView, PinView, BaseMiningView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupForPhase(currentPhase: ChangePinActivity.Companion.CurrentPhase)
}
