package com.enecuum.androidapp.presentation.presenter.balance

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.presentation.view.balance.BalanceView

@InjectViewState
class BalancePresenter : MvpPresenter<BalanceView>() {
    fun onCreate() {
        //TODO: fill with real values
        viewState.displayCurrencyRates(7.999999, 7.999999)
        viewState.displayBalances(30.0, 30.0)
        viewState.displayPoints(1.0)
        viewState.displayKarma(1.0)
        viewState.displayPercentage(1.0, 1.0)
    }

    fun onTokensClick() {

    }
}
