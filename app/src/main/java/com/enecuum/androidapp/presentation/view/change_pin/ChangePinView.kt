package com.enecuum.androidapp.presentation.view.change_pin

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuum.androidapp.presentation.view.BaseMiningView
import com.enecuum.androidapp.presentation.view.ButtonStateView
import com.enecuum.androidapp.presentation.view.BasePinView
import com.enecuum.androidapp.presentation.view.PagerView
import com.enecuum.androidapp.ui.activity.change_pin.ChangePinActivity

interface ChangePinView : ButtonStateView, BaseMiningView, PagerView
