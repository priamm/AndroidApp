package com.enecuum.androidapp.presentation.presenter.balance

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.inherited.models.MicroblockResponse
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.presentation.view.balance.BalanceView
import com.enecuum.androidapp.ui.activity.testActivity.CustomBootNodeFragment
import com.enecuum.androidapp.ui.activity.testActivity.PoaService


@InjectViewState
class BalancePresenter : MvpPresenter<BalanceView>() {

    private lateinit var poaService: PoaService

    val sharedPreferences = EnecuumApplication.applicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

    val microblockList = hashMapOf<String, MicroblockResponse>()

    val custom = sharedPreferences.getBoolean(CustomBootNodeFragment.customBN, false)
    val customPath = sharedPreferences.getString(CustomBootNodeFragment.customBNIP, CustomBootNodeFragment.BN_PATH_DEFAULT);
    val customPort = sharedPreferences.getString(CustomBootNodeFragment.customBNPORT, CustomBootNodeFragment.BN_PORT_DEFAULT);
    val path = if (custom) customPath else CustomBootNodeFragment.BN_PATH_DEFAULT
    val port = if (custom) customPort else CustomBootNodeFragment.BN_PORT_DEFAULT

    fun onCreate() {
        poaService = PoaService(EnecuumApplication.applicationContext(),
                path,
                port,
                onTeamSize = object : PoaService.onTeamListener {
                    override fun onTeamSize(size: Int) {
                        Handler(Looper.getMainLooper()).post {
                            viewState.displayTeamSize(size);
                        }
                    }
                },
                onMicroblockCountListerer = object : PoaService.onMicroblockCountListener {
                    override fun onMicroblockCountAndLast(count: Int, microblockResponse: MicroblockResponse, microblockSignature: String) {
                        microblockList.put(microblockSignature, microblockResponse);
                        viewState.displayTransactionsHistory(microblockList.keys.toList())
                        viewState.displayMicroblocks(10 * count);
                    }
                },
                onConnectedListner = object : PoaService.onConnectedListener {
                    override fun onConnectionError() {
                        viewState.showConnectionError()
                    }

                    override fun onStartConnecting() {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(true)
                            viewState.showLoading()
                        }
                    }

                    override fun onDisconnected() {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(true)
                            viewState.hideProgress()
                        }
                    }

                    override fun onConnected(ip: String, port: String) {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(false)
                            viewState.showProgress()
                            viewState.hideLoading()
                        }
                    }
                },
                balanceListener = object : PoaService.BalanceListener {
                    override fun onBalance(amount: Int) {
                        viewState.setBalance(amount)
                    }
                }
        )
    }

    fun onTokensClick() {
        EnecuumApplication.navigateToFragment(FragmentType.Tokens, TabType.Home)
    }


    fun onMiningToggle() {
        if (::poaService.isInitialized) {
            if (poaService.isConnected()) {
                poaService.disconnect()
            } else {
                poaService.connect()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::poaService.isInitialized) {
            if (poaService.isConnected()) {
                poaService.disconnect()
            }
        }
    }
}
