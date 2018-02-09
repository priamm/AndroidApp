package com.enecuum.androidapp.ui.fragment.send_single_tab

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.send_single_tab.SendSingleTabView
import com.enecuum.androidapp.presentation.presenter.send_single_tab.SendSingleTabPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuum.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_send_single_tab.*

class SendSingleTabFragment : MvpAppCompatFragment(), SendSingleTabView {
    companion object {
        const val TAG = "SendSingleTabFragment"
        const val SEND_MODE = "sendMode"
        fun newInstance(sendMode: SendReceiveMode, transaction: Transaction?): SendSingleTabFragment {
            val fragment = SendSingleTabFragment()
            val args = Bundle()
            fragment.arguments = args
            args.putSerializable(SEND_MODE, sendMode)
            args.putSerializable(TRANSACTION, transaction)
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SendSingleTabPresenter

    private var isSetupFinished = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_single_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.handleArgs(arguments)
        addressText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.onAddressChanged(addressText.text.toString())
            }
        })
    }

    override fun setupWithAmount(amount: Float) {
        balanceAmount.text = String.format("%s %.8f", getString(R.string.your_balance), amount)
        sendMax.setOnClickListener {
            amountText.setText(amount.toString())
        }
        amountText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.onAmountTextChanged(amountText.text.toString())
            }
        })
        isSetupFinished = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && isSetupFinished) {
            presenter.refreshButtonState(addressText.text.toString(), amountText.text.toString())
        }
    }

    override fun changeAddress(newValue: String) {
        addressText.setText(newValue)
    }

    override fun setupWithTransaction(transaction: Transaction) {
        addressText.setText(transaction.address)
        amountText.setText(transaction.amount.toString())
        presenter.refreshButtonState(addressText.text.toString(), amountText.text.toString())
    }
}
