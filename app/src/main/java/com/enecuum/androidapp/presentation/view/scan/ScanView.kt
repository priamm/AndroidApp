package com.enecuum.androidapp.presentation.view.scan

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ScanView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun requestPermissions()
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onPermissionsGranted()
}
