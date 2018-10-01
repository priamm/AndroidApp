package com.enecuumwallet.androidapp.ui.fragment.balance

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.events.MainActivityStopped
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.presenter.balance.BalancePresenter
import com.enecuumwallet.androidapp.presentation.view.balance.BalanceView
import com.enecuumwallet.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuumwallet.androidapp.utils.TransactionsHistoryRenderer
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_balance.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

class BalanceFragment : NoBackFragment(), BalanceView {

    companion object {
        const val FORMAT = "%s %.8f"
        const val TAG = "BalanceFragment"
        private var instance: BalanceFragment? = null
        fun singleton(): BalanceFragment {
            if (instance == null)
                instance = BalanceFragment()
            return instance!!
        }
    }

    @InjectPresenter
    lateinit var presenter: BalancePresenter

    lateinit var pd: ProgressDialog;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    val startMiningReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            presenter.onMiningToggle();
        }
    }
    private val RESTART_ACTION: String = "restart_action"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.registerReceiver(startMiningReciever, IntentFilter(RESTART_ACTION))
        pd = ProgressDialog(view.context)

        pd.setMessage("Connecting...")
        presenter.onCreate()

        RxView.clicks(start)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    presenter.onMiningToggle();
                }

        if (PersistentStorage.getAutoMiningStart()) {
            Handler(Looper.getMainLooper()).postDelayed({
                presenter.onMiningToggle();
                Toast.makeText(view.context, "Restoring after crash", Toast.LENGTH_LONG).show()
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(Intent(RESTART_ACTION))
                PersistentStorage.setAutoMiningStart(false)
            }, 10000);
        }

//        //////REMOVE THIS ONLY, FOR CRASH TESTING
//        Handler().postDelayed({
//            Utils.crashMe()
//        }, 30000)

        tokens.setOnClickListener({
            presenter.onTokensClick()
        })
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.turquoise_blue_three),
                android.graphics.PorterDuff.Mode.SRC_IN);

//        presenter.onCreate()
        setHasOptionsMenu(true)
        TransactionsHistoryRenderer.configurePanelListener(slidingLayout, panelHint)
    }

    override fun onDestroyView() {
        context?.unregisterReceiver(startMiningReciever)
        super.onDestroyView()
    }

    override fun displayCurrencyRates(enq2Usd: Double, enq2Btc: Double) {
        enqBtc.text = String.format("%f ENQ/BTC", enq2Btc)
        enqUsd.text = String.format("%f ENQ/USD", enq2Usd)
    }

    override fun displayBalances(enq: Double, enqPlus: Double) {
        enqBalance.text = String.format("ENQ %.8f", enq)
        enqPlusBalance.text = String.format("ENQ+ %.8f", enqPlus)
    }


    override fun displayTeamSize(teamSize: Int) {
        Handler(Looper.getMainLooper()).post {
//            minedText.post { minedText.text = if (teamSize > 0) "Waiting" else "Work in progress"; }
        }
    }


    override fun displayTransactionsHistory(transactionsList: List<String>) {
        this.context?.let {
            TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory, it)
        }

    }

    override fun getTitle(): String {
        if (activity == null)
            return ""
        return activity!!.getString(R.string.mining)
    }

    override fun setBalance(balance: Int?) {
        Handler(Looper.getMainLooper()).post {
            enqBalance.text = "ENQ " + balance?.toString();
        }
    }

    override fun showProgress() {
        Handler(Looper.getMainLooper()).post {
            progressBar.visibility = View.VISIBLE
        }

    }

    override fun hideProgress() {
        Handler(Looper.getMainLooper()).post {
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun changeButtonState(isStart: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (isStart) {
                start.text = getText(R.string.start_mining)
            } else {
                start.text = getText(R.string.stop_mining)
            }
        }
    }

    override fun updateProgressMessage(str: String) {
        Handler(Looper.getMainLooper()).post {
            pd.setMessage(str)
        }
    }


    override fun showLoading() {
        Handler(Looper.getMainLooper()).post {
            pd.show()
        }
    }

    override fun showConnectionError(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, getString(R.string.connection_error) + ": " + message, Toast.LENGTH_LONG).show()
        }

    }

    override fun hideLoading() {
        Handler(Looper.getMainLooper()).post {
            if (pd.isShowing) {
                pd.dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }


}
