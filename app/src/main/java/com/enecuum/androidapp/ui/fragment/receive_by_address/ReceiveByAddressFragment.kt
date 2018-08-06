package com.enecuum.androidapp.ui.fragment.receive_by_address

import android.os.Bundle
import android.view.*
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.receive_by_address.ReceiveByAddressView
import com.enecuum.androidapp.presentation.presenter.receive_by_address.ReceiveByAddressPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.ui.activity.main.MainActivity
import com.enecuum.androidapp.ui.adapters.ReceiveEnqTabsAdapter
import com.enecuum.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuum.androidapp.utils.TransactionsHistoryRenderer
import kotlinx.android.synthetic.main.fragment_receive_by_address.*

class ReceiveByAddressFragment : NoBackFragment(), ReceiveByAddressView {
    companion object {
        const val TAG = "ReceiveByAddressFragment"

        fun newInstance(args: Bundle): ReceiveByAddressFragment {
            val fragment = ReceiveByAddressFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ReceiveByAddressPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_receive_by_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate(arguments)
        setHasOptionsMenu(true)
        receive.setOnClickListener {
            presenter.onReceiveClick(activity is MainActivity)
        }
    }

    override fun getTitle(): String = getString(R.string.receive)

    override fun displayTransactionsHistory(transactionsList: List<Transaction>) {
//        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()

    }

    override fun setupForTransaction(transaction: Transaction) {
        slidingLayout.panelHeight = 0
        val adapter = ReceiveEnqTabsAdapter(childFragmentManager, context!!)
        adapter.setTransaction(transaction)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        when(transaction.mode) {
            SendReceiveMode.Enq -> viewPager.currentItem = 0
            SendReceiveMode.EnqPlus -> viewPager.currentItem = 1
        }
    }

    override fun setupNormally() {
        viewPager.adapter = ReceiveEnqTabsAdapter(childFragmentManager, context!!)
        tabLayout.setupWithViewPager(viewPager)
        TransactionsHistoryRenderer.configurePanelListener(slidingLayout, panelHint)
    }

    override fun changeButtonState(enable: Boolean) {
        receive.isEnabled = enable
    }
}
