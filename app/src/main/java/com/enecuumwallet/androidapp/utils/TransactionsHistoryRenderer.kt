package com.enecuumwallet.androidapp.utils

import android.content.Context
import android.support.v4.widget.SlidingPaneLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.models.MiningHistoryItem
import com.enecuumwallet.androidapp.models.Transaction
import com.enecuumwallet.androidapp.models.inherited.models.MicroblockResponse
import com.enecuumwallet.androidapp.ui.adapters.MiningHistoryAdapter
import com.enecuumwallet.androidapp.ui.adapters.TransactionsListAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout

/**
 * Created by oleg on 31.01.18.
 */
object TransactionsHistoryRenderer {
    fun displayTransactionsInRecyclerView(transactionsList : List<String>, recyclerView: RecyclerView, context : Context) {
        val adapter = TransactionsListAdapter(transactionsList, context )
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun displayMiningHistoryInRecyclerView(transactionsList : List<MiningHistoryItem>, recyclerView: RecyclerView) {
        val adapter = MiningHistoryAdapter(transactionsList)
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun configurePanelListener(panel: SlidingUpPanelLayout, icon: ImageView) {
        panel.addPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener, SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    icon.setImageResource(R.drawable.show_history)
                    return
                }
                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    icon.setImageResource(R.drawable.hide_history)
                }
            }

            override fun onPanelSlide(panel: View, slideOffset: Float) {}

            override fun onPanelClosed(panel: View) {}

            override fun onPanelOpened(panel: View) {}

        })
    }
}