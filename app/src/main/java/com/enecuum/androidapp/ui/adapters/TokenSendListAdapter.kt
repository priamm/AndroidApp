package com.enecuum.androidapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.enecuum.androidapp.R
import com.enecuum.androidapp.events.TokensSelected
import com.enecuum.androidapp.models.TokenInfo
import com.enecuum.androidapp.ui.adapters.holders.TokenViewHolder
import com.enecuum.androidapp.ui.fragment.tokens_single.TokensSingleFragment
import kotlinx.android.synthetic.main.item_tokens_list.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by oleg on 30.01.18.
 */
class TokenSendListAdapter(private val data: List<TokenInfo>, private val mode: TokensSingleFragment.Companion.Mode) : RecyclerView.Adapter<TokenViewHolder>() {
    private val selectedIds = mutableListOf<String>()
    private var currentData = data

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TokenViewHolder =
            TokenViewHolder(LayoutInflater.from(parent?.context).inflate(
                    R.layout.item_tokens_list,
                    parent,
                    false))

    override fun getItemCount(): Int = currentData.size

    override fun onBindViewHolder(holder: TokenViewHolder?, position: Int) {
        val tokenInfo = currentData[position]
        if(mode == TokensSingleFragment.Companion.Mode.JettonMode) {
            holder?.itemView?.checkBox?.visibility = View.INVISIBLE
        } else {
            holder?.itemView?.checkBox?.visibility = View.VISIBLE
            holder?.itemView?.checkBox?.setOnCheckedChangeListener(null)
            holder?.itemView?.checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked) {
                    selectedIds.add(tokenInfo.id)
                } else {
                    selectedIds.remove(tokenInfo.id)
                }
                EventBus.getDefault().post(TokensSelected(selectedIds))
            }
        }
        holder?.itemView?.amount?.text = String.format("%.8f", tokenInfo.amount)
        holder?.itemView?.tokenType?.text = tokenInfo.tokenType
    }

    fun filter(filterText: String) {
        currentData = if(filterText.isEmpty()) {
            data
        } else {
            data.filter { it.tokenType.contains(filterText, true)
                    || String.format("%.8f", it.amount).contains(filterText, true) }
        }
        notifyDataSetChanged()
    }
}