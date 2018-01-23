package com.enecuum.androidapp.presentation.view.new_account_qr

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface NewAccountQrView : MvpView {
    fun showQrCode(key: String)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun requestPermissions()
}
