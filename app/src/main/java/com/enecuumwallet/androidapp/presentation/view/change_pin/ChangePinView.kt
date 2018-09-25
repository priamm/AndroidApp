package com.enecuumwallet.androidapp.presentation.view.change_pin

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuumwallet.androidapp.presentation.view.BaseMiningView
import com.enecuumwallet.androidapp.presentation.view.ButtonStateView
import com.enecuumwallet.androidapp.presentation.view.BasePinView
import com.enecuumwallet.androidapp.presentation.view.PagerView
import com.enecuumwallet.androidapp.ui.activity.change_pin.ChangePinActivity

interface ChangePinView : ButtonStateView, BaseMiningView, PagerView
