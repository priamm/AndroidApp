package com.enecuum.androidapp.ui.fragment.send_finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.send_finish.SendFinishView
import com.enecuum.androidapp.presentation.presenter.send_finish.SendFinishPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.models.Currency
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.ui.base_ui_primitives.BackTitleFragment
import com.enecuum.androidapp.utils.TransactionsHistoryRenderer
import kotlinx.android.synthetic.main.fragment_send_finish.*

class SendFinishFragment : BackTitleFragment(), SendFinishView {
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
        presenter.handleArgs(arguments)
        send.setOnClickListener {
            presenter.onSendClick()
        }
        setHasOptionsMenu(true)
    }

    override fun getTitle(): String = getString(R.string.confirm_send)

    override fun displayTransactionsHistory(transactionsList: List<Transaction>) {
        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun setupWithData(address: String?, amount: Float?, currency: Currency) {
        addressText.text = address
        amountCurrency.text = String.format("%.8f %s", amount, currency.value)
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }

    override fun hideHistory() {
        mainLayout.panelHeight = 0
    }
}
