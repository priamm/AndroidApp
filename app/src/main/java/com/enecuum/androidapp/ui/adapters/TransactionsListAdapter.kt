package com.enecuum.androidapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import kotlinx.android.synthetic.main.item_transactions_list.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by oleg on 30.01.18.
 */
class TransactionsListAdapter(private val data : List<Transaction>) : RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TransactionViewHolder =
            TransactionViewHolder(LayoutInflater.from(parent?.context).inflate(
                    R.layout.item_transactions_list,
                    parent,
                    false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TransactionViewHolder?, position: Int) {
        val transaction = data[position]
        val dateFormatter = SimpleDateFormat("dd.MM")
        val timeFormatter = SimpleDateFormat("HH.mm")
        val date = Date(transaction.timestamp)
        holder?.itemView?.dateText?.text = dateFormatter.format(date)
        holder?.itemView?.timeText?.text = timeFormatter.format(date)
        holder?.itemView?.address?.text = transaction.address
        holder?.itemView?.amount?.text = String.format("%.8f ENQ", transaction.amount)
        when(transaction.transactionType) {
            TransactionType.Send -> {
                holder?.itemView?.icon?.setImageResource(R.drawable.send_little)
            }
            TransactionType.Receive -> {
                holder?.itemView?.icon?.setImageResource(R.drawable.teceive_little)
            }
        }

    }
}