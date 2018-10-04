package com.enecuumwallet.androidapp.ui.fragment.send_finish

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.models.Currency
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.presentation.presenter.send_finish.SendFinishPresenter
import com.enecuumwallet.androidapp.presentation.view.send_finish.SendFinishView
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackTitleFragment
import kotlinx.android.synthetic.main.fragment_send_finish.*

class SendFinishFragment : BackTitleFragment(), SendFinishView {


    lateinit var pd: ProgressDialog;

    override fun doOnStartSending() {
        Handler(Looper.getMainLooper()).post {
            pd.setTitle("Sending...")
            pd.show()
        }

    }

    override fun doOnResult(isSuccess: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (pd.isShowing) {
                pd.dismiss()
            }

            if (isSuccess) {
                EnecuumApplication.exitFromCurrentFragment()
            }
        }

    }


    companion object {
        const val TAG = "SendFinishFragment"

        fun newInstance(args: Bundle): SendFinishFragment {
            val fragment = SendFinishFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SendFinishPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pd = ProgressDialog(activity)
        presenter.handleArgs(arguments)
        send.setOnClickListener {
            presenter.onSendClick()
        }

        setHasOptionsMenu(true)
    }

    override fun getTitle(): String = getString(R.string.confirm_send)

    override fun displayTransactionsHistory(transactionsList: List<Transaction>) {
//        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun setupWithData(address: String?, amount: Int?, currency: Currency) {
        addressText.text = address
        amountCurrency.text = String.format("%d %s", amount, currency.value)
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }

    override fun hideHistory() {
        mainLayout.panelHeight = 0
    }
}
