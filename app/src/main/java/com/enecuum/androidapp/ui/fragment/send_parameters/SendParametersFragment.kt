package com.enecuum.androidapp.ui.fragment.send_parameters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.presentation.presenter.send_parameters.SendParametersPresenter
import com.enecuum.androidapp.presentation.view.send_parameters.SendParametersView
import com.enecuum.androidapp.ui.adapters.SendEnqTabsAdapter
import com.enecuum.androidapp.ui.base_ui_primitives.NoBackFragment
import com.enecuum.androidapp.utils.TransactionsHistoryRenderer
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_send_parameters.*

class SendParametersFragment : NoBackFragment(), SendParametersView {

    companion object {
        const val TAG = "SendParametersFragment"
        const val AMOUNT = "amount"
        const val CURRENCY = "currency"
        const val ADDRESS = "address"
        fun newInstance(): SendParametersFragment {
            val fragment: SendParametersFragment = SendParametersFragment()
            val args: Bundle = Bundle()
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
        presenter.onCreate()
        viewPager.adapter = SendEnqTabsAdapter(childFragmentManager, context!!)
        tabLayout.setupWithViewPager(viewPager)
        send.setOnClickListener {
            presenter.onSendClick()
        }
        setHasOptionsMenu(true)
    }

    override fun getTitle(): String = getString(R.string.send)

    override fun displayTransactionsHistory(transactionsList: List<Transaction>) {
        TransactionsHistoryRenderer.displayTransactionsInRecyclerView(transactionsList, transactionsHistory)
    }

    override fun handleKeyboardVisibility(visible: Boolean) {
        transactionsHistory.visibility = if(visible) View.GONE else View.VISIBLE
    }

    override fun changeButtonState(enable: Boolean) {
        send.isEnabled = enable
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.qr_menu, menu)
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
}
