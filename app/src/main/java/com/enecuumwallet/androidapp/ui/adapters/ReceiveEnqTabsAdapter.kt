package com.enecuumwallet.androidapp.ui.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.enecuumwallet.androidapp.models.SendReceiveMode
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.ui.fragment.receive_single_tab.ReceiveSingleTabFragment

/**
 * Created by oleg on 30.01.18.
 */
class ReceiveEnqTabsAdapter(fm: FragmentManager?, val context: Context) : FragmentPagerAdapter(fm) {
    private var transaction: Transaction? = null

    override fun getItem(position: Int): Fragment {
        val currentMode = if(position == 0)
            SendReceiveMode.Enq
        else
            SendReceiveMode.EnqPlus
        var currentTransaction: Transaction? = null
        if(transaction != null && transaction!!.mode == currentMode) {
            currentTransaction = transaction
        }
        return ReceiveSingleTabFragment.newInstance(currentMode, currentTransaction)
    }

    override fun getPageTitle(position: Int): CharSequence? =
            if(position == 0)
                "ENQ"
            else
                "ENQ+"

    override fun getCount(): Int = 2

    fun setTransaction(transaction: Transaction) {
        this.transaction = transaction
    }
}