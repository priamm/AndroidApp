package com.enecuum.androidapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.models.inherited.models.MicroblockResponse
import com.enecuum.androidapp.ui.adapters.holders.TransactionViewHolder
import kotlinx.android.synthetic.main.item_transactions_list.view.*

/**
 * Created by oleg on 30.01.18.
 */
class TransactionsListAdapter(private val data: List<MicroblockResponse>) : RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
            TransactionViewHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_transactions_list,
                    parent,
                    false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = data[position]
//        val dateFormatter = SimpleDateFormat("dd.MM", Locale.getDefault())
//        val timeFormatter = SimpleDateFormat("HH.mm", Locale.getDefault())
//        val date = Date(transaction.timestamp)
//        holder.itemView.dateText?.text = dateFormatter.format(date)
//        holder.itemView.timeText?.text = timeFormatter.format(date)
//        "http://82.202.212.120/?#/explorer/wallet/LB4JCsuWPqYuB99qe9cCS8bucpCWHrx5qg8PvLFpTfhU"
        holder.itemView.address?.text = transaction.microblock.msg.K_hash
        holder.itemView.amount?.text = "10 ENQ"
//        when(transaction.transactionType) {
//            TransactionType.Send -> {
//                holder.itemView.icon?.setImageResource(R.drawable.send_little)
//            }
//            TransactionType.Receive -> {
//                holder.itemView.icon?.setImageResource(R.drawable.receive_little)
//            }
//        }
//        holder.itemView.repeatTransaction?.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putSerializable(TRANSACTION, transaction)
//            EnecuumApplication.navigateToActivity(ScreenType.TransactionDetails, bundle)
//        }
    }
}