package com.enecuum.androidapp.ui.fragment.receive_by_address

import android.os.Bundle
import android.view.*
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.receive_by_address.ReceiveByAddressView
import com.enecuum.androidapp.presentation.presenter.receive_by_address.ReceiveByAddressPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.ui.adapters.ReceiveEnqTabsAdapter
import com.enecuum.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuum.androidapp.utils.TransactionsHistoryRenderer
import kotlinx.android.synthetic.main.fragment_receive_by_address.*

class ReceiveByAddressFragment : NoBackFragment(), ReceiveByAddressView {
    companion object {
        const val TAG = "ReceiveByAddressFragment"

        fun newInstance(): ReceiveByAddressFragment {
            val fragment = ReceiveByAddressFragment()
            val args = Bundle()
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
        presenter.onCreate()
        viewPager.adapter = ReceiveEnqTabsAdapter(childFragmentManager, context!!)
        tabLayout.setupWithViewPager(viewPager)
        setHasOptionsMenu(true)
    }

    override fun getTitle(): String = getString(R.string.receive)

    override fun displayTransactionsHistory(transactionsList: List<Transaction>) {
        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.qr_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.qr) {
            presenter.onQrClick()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if(menu != null && !menu!!.hasVisibleItems()) {
            menuInflater?.inflate(R.menu.qr_menu, menu)
        }
    }

    override fun handleKeyboardVisibility(visible: Boolean) {
        transactionsHistory.visibility = if(visible) View.GONE else View.VISIBLE
    }
}
