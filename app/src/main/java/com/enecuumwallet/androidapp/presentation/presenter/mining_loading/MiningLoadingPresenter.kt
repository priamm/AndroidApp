package com.enecuumwallet.androidapp.presentation.presenter.mining_loading

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.presentation.view.mining_loading.MiningLoadingView
import java.util.*


@InjectViewState
class MiningLoadingPresenter : MvpPresenter<MiningLoadingView>() {
    companion object {
        const val TEAM_COUNT = "TEAM_COUNT"
        private const val TEAM_SIZE = 64
    }
    fun onCreate() {
        //TODO: load team members from dag
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val bundle = Bundle()
            val random = Random(Date().time)
            bundle.putInt(TEAM_COUNT, 1 + random.nextInt(TEAM_SIZE-1))
            EnecuumApplication.fragmentCicerone().router.replaceScreen(FragmentType.MiningJoinTeam.toString(), bundle)
        }, 1000)
    }

}
