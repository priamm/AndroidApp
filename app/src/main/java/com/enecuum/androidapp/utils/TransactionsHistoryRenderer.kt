package com.enecuum.androidapp.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.ui.adapters.TransactionsListAdapter

/**
 * Created by oleg on 31.01.18.
 */
object TransactionsHistoryRenderer {
    fun displayTransactionsInRecyclerView(transactionsList : List<Transaction>, recyclerView: RecyclerView) {
        val adapter = TransactionsListAdapter(transactionsList)
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}