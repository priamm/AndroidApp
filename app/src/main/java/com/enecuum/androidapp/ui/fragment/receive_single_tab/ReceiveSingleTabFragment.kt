package com.enecuum.androidapp.ui.fragment.receive_single_tab

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.presentation.presenter.receive_single_tab.ReceiveSingleTabPresenter
import com.enecuum.androidapp.presentation.view.receive_single_tab.ReceiveSingleTabView
import com.enecuum.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuum.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_receive_single_tab.*

class ReceiveSingleTabFragment : MvpAppCompatFragment(), ReceiveSingleTabView {
    companion object {
        const val TAG = "ReceiveSingleTabFragment"
        const val RECEIVE_MODE = "receiveMode"
        fun newInstance(receiveMode: SendReceiveMode, currentTransaction: Transaction?): ReceiveSingleTabFragment {
            val fragment = ReceiveSingleTabFragment()
            val args = Bundle()
            args.putSerializable(RECEIVE_MODE, receiveMode)
            args.putSerializable(TRANSACTION, currentTransaction)
            fragment.arguments = args
            return fragment
        }
    }

    private var isSetupFinished = false

    @InjectPresenter
    lateinit var presenter: ReceiveSingleTabPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_receive_single_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.handleArgs(arguments)
        copy.setOnClickListener {
            presenter.onCopyClicked(addressText.text.toString())
        }
        addressText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.onAddressTextChanged(addressText.text.toString())
            }
        })
    }

    override fun setupWithAmount(totalAmount: Float) {
        balanceAmount.text = String.format("%s %.8f", getString(R.string.your_balance), totalAmount)
        isSetupFinished = true
    }

    override fun setupWithTransaction(transaction: Transaction) {
        addressText.setText(transaction.address)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && isSetupFinished) {
            presenter.onAddressTextChanged(addressText.text.toString())
        }
    }
}
