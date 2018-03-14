package com.enecuum.androidapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.MiningHistoryItem
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.ui.adapters.holders.MiningHistoryViewHolder
import kotlinx.android.synthetic.main.item_mining_list.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by oleg on 30.01.18.
 */
class MiningHistoryAdapter(private val data : List<MiningHistoryItem>) : RecyclerView.Adapter<MiningHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiningHistoryViewHolder =
            MiningHistoryViewHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_mining_list,
                    parent,
                    false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MiningHistoryViewHolder, position: Int) {
        val transaction = data[position]
        val dateFormatter = SimpleDateFormat("dd.MM", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH.mm", Locale.getDefault())
        val date = Date(transaction.startTimestamp)
        holder.itemView?.date1Text?.text = dateFormatter.format(date)
        holder.itemView?.time1Text?.text = timeFormatter.format(date)
        val date2 = Date(transaction.endTimestamp)
        holder.itemView?.date2Text?.text = dateFormatter.format(date2)
        holder.itemView?.time2Text?.text = timeFormatter.format(date2)
        holder.itemView?.amount?.text = String.format("%.8f ENQ", transaction.amount)


    }
}