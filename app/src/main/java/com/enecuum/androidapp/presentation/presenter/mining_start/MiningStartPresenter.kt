package com.enecuum.androidapp.presentation.presenter.mining_start

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.presentation.view.mining_start.MiningStartView

@InjectViewState
class MiningStartPresenter : MvpPresenter<MiningStartView>() {
    fun onStartClick() {
        EnecuumApplication.fragmentCicerone().router.replaceScreen(FragmentType.MiningLoading.toString())
    }

}
