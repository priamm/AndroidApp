package com.enecuumwallet.androidapp.presentation.view.send_single_tab

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuumwallet.androidapp.models.Transaction

interface SendSingleTabView : MvpView {
    fun setupWithAmount(amount: Int)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun changeAddress(newValue: String)
    fun setupWithTransaction(transaction: Transaction)
}
