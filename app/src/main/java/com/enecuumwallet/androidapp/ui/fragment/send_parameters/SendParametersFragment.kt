package com.enecuumwallet.androidapp.ui.fragment.send_parameters

import android.os.Bundle
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.models.SendReceiveMode
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.presentation.presenter.send_parameters.SendParametersPresenter
import com.enecuumwallet.androidapp.presentation.view.send_parameters.SendParametersView
import com.enecuumwallet.androidapp.ui.activity.main.MainActivity
import com.enecuumwallet.androidapp.ui.adapters.SendEnqTabsAdapter
import com.enecuumwallet.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuumwallet.androidapp.utils.TransactionsHistoryRenderer
import kotlinx.android.synthetic.main.fragment_send_parameters.*

class SendParametersFragment : NoBackFragment(), SendParametersView {

    companion object {
        const val TAG = "SendParametersFragment"
        const val AMOUNT = "amount"
        const val CURRENCY = "currency"
        const val ADDRESS = "address"
        const val IS_HISTORY_VISIBLE = "isHistoryVisible"
        fun newInstance(args: Bundle): SendParametersFragment {
            val fragment = SendParametersFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SendParametersPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_parameters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate(arguments)
        send.setOnClickListener {
            presenter.onSendClick(activity is MainActivity)
        }
    }

    override fun getTitle(): String = getString(R.string.my_wallet)

    override fun displayTransactionsHistory(transactionsList: List<Transaction>) {
//        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun changeButtonState(enable: Boolean) {
        send.isEnabled = enable
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
//        inflater?.inflate(R.menu.qr_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.qr) {
            presenter.onQrCodeClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if(menu != null && !menu!!.hasVisibleItems()) {
            menuInflater?.inflate(R.menu.qr_menu, menu)
        }
    }

    override fun setupForTransaction(transaction: Transaction) {
        slidingLayout.panelHeight = 0
        val adapter = SendEnqTabsAdapter(childFragmentManager, context!!)
        adapter.setTransaction(transaction)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        setHasOptionsMenu(false)
        when(transaction.mode) {
            SendReceiveMode.Enq -> viewPager.currentItem = 0
            SendReceiveMode.EnqPlus -> viewPager.currentItem = 1
        }
    }

    override fun setupNormally() {
        viewPager.adapter = SendEnqTabsAdapter(childFragmentManager, context!!)
        tabLayout.setupWithViewPager(viewPager)
        TransactionsHistoryRenderer.configurePanelListener(slidingLayout, panelHint)
        setHasOptionsMenu(true)
    }
}
