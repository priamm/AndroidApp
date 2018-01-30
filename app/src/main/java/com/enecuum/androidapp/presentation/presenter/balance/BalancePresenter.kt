package com.enecuum.androidapp.presentation.presenter.balance

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType
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
        val transactionsList = listOf(
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…"),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…")
        )
        viewState.displayTransactionsHistory(transactionsList)
    }

    fun onTokensClick() {
        EnecuumApplication.navigateToFragment(FragmentType.Tokens, TabType.Home)
    }
}
