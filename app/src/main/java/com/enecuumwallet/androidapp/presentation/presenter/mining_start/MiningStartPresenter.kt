package com.enecuumwallet.androidapp.presentation.presenter.mining_start

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.presentation.view.mining_start.MiningStartView

@InjectViewState
class MiningStartPresenter : MvpPresenter<MiningStartView>() {
    fun onStartClick() {
        EnecuumApplication.fragmentCicerone().router.replaceScreen(FragmentType.MiningLoading.toString())
    }

}
