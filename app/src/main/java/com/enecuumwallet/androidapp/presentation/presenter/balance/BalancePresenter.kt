package com.enecuumwallet.androidapp.presentation.presenter.balance

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.models.inherited.models.MicroblockResponse
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.presentation.view.balance.BalanceView
import com.enecuumwallet.androidapp.ui.activity.testActivity.CustomBootNodeFragment
import com.enecuumwallet.androidapp.ui.activity.testActivity.PoaClient
import com.jraska.console.Console
import java.util.*
import android.os.CountDownTimer
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage


@InjectViewState
class BalancePresenter : MvpPresenter<BalanceView>() {

    private lateinit var poaClient: PoaClient

    val sharedPreferences = EnecuumApplication.applicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

    val microblockList = hashMapOf<String, MicroblockResponse>()

    val customBn = sharedPreferences.getBoolean(CustomBootNodeFragment.customBN, false)

    val customPath = sharedPreferences.getString(CustomBootNodeFragment.customBNIP, CustomBootNodeFragment.BN_PATH_DEFAULT);
    val customPort = sharedPreferences.getString(CustomBootNodeFragment.customBNPORT, CustomBootNodeFragment.BN_PORT_DEFAULT);

    val pathBn = if (customBn) customPath else CustomBootNodeFragment.BN_PATH_DEFAULT
    val portBn = if (customBn) customPort else CustomBootNodeFragment.BN_PORT_DEFAULT

    val customTn = sharedPreferences.getBoolean(CustomBootNodeFragment.customTM, false)

    val customPathTn = sharedPreferences.getString(CustomBootNodeFragment.customTNIP, CustomBootNodeFragment.TN_PATH_DEFAULT);
    val customPortTn = sharedPreferences.getString(CustomBootNodeFragment.customTNPORT, CustomBootNodeFragment.TN_PORT_DEFAULT);

    val pathTn = if (customTn) customPathTn else CustomBootNodeFragment.TN_PATH_DEFAULT
    val portTn = if (customTn) customPortTn else CustomBootNodeFragment.TN_PORT_DEFAULT

    fun onCreate() {
        if (!::poaClient.isInitialized) {
            poaClient = PoaClient(EnecuumApplication.applicationContext(),
                    BN_PATH = pathBn,
                    BN_PORT = portBn,
                    TEAM_WS_IP = pathTn,
                    TEAM_WS_PORT = portTn,
                    onTeamSizeListener = object : PoaClient.onTeamListener {
                        override fun onTeamSize(size: Int) {
                            Handler(Looper.getMainLooper()).post {
                                viewState.displayTeamSize(size);
                            }
                        }
                    },
                    onMicroblockCountListerer = object : PoaClient.onMicroblockCountListener {
                        override fun onMicroblockCountAndLast(microblockResponse: MicroblockResponse, microblockSignature: String) {
                            microblockList[microblockSignature] = microblockResponse
                            viewState.displayTransactionsHistory(microblockList.keys.toList())
                        }
                    },
                    onConnectedListner = object : PoaClient.onConnectedListener {
                        override fun doReconnect() {
                            //prevent show disconnected
                            if (!disconnectedByUser) {
                                object : CountDownTimer(5000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        viewState.updateProgressMessage("Connecting...  " + millisUntilFinished / 1000 + " sec")

                                    }

                                    override fun onFinish() {
                                        viewState.updateProgressMessage("Connecting...")
                                        connect()
                                    }

                                }.start()
                                viewState.showProgress()
                                disconnectedByUser = false
                            }
                        }

                        override fun onConnectionError(message: String) {
                            viewState.showConnectionError(message)
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
                                viewState.hideLoading()
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
                    balanceListener = object : PoaClient.BalanceListener {
                        override fun onBalance(amount: Int) {
                            viewState.setBalance(amount)
                            PersistentStorage.setCurrentBalance(amount)
                        }
                    }
            )
        }
    }


    internal inner class CleanTask : TimerTask() {
        override fun run() {
            Console.clear()
        }
    };
    fun onTokensClick() {
        EnecuumApplication.navigateToFragment(FragmentType.Tokens, TabType.Balance)
    }


    private var disconnectedByUser: Boolean = false

    fun onMiningToggle() {
        if (::poaClient.isInitialized) {
            if (!poaClient.isConnected()) {
                connect()
            } else {
                disconnectedByUser = true
                disconnect()
            }
        }
    }

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
    }

    fun connect() {
        if (::poaClient.isInitialized) {
            if (!poaClient.isConnected()) {
                poaClient.connect()
            }
        }
    }

    fun disconnect() {
        if (::poaClient.isInitialized) {
            if (poaClient.isConnected()) {
                poaClient.disconnect()
            }
        }
    }
}
