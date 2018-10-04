package com.enecuumwallet.androidapp.ui.fragment.send_single_tab

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.models.SendReceiveMode
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.presenter.send_single_tab.SendSingleTabPresenter
import com.enecuumwallet.androidapp.presentation.view.send_single_tab.SendSingleTabView
import com.enecuumwallet.androidapp.ui.activity.testActivity.Base58
import com.enecuumwallet.androidapp.ui.activity.transaction_details.TransactionDetailsActivity.Companion.TRANSACTION
import com.enecuumwallet.androidapp.utils.SimpleTextWatcher
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

        val wallet = PersistentStorage.getWallet()
        myAddressText.setText(wallet)

        copy.setOnClickListener {
            val clipboard = view.context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("wallet", wallet)
            clipboard.setPrimaryClip(clip)
        }

        presenter.handleArgs(arguments)
        addressText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.onAddressChanged(addressText.text.toString())
            }
        })
    }

    override fun setupWithAmount(amount: Int) {
        balanceAmount.text = String.format("%s %d", getString(R.string.your_balance), amount)
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
        if (isVisibleToUser && isSetupFinished) {
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
