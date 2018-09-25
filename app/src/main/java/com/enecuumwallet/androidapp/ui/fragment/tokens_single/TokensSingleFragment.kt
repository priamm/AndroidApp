package com.enecuumwallet.androidapp.ui.fragment.tokens_single

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.view.tokens_single.TokensSingleView
import com.enecuumwallet.androidapp.presentation.presenter.tokens_single.TokensSinglePresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.models.TokenInfo
import com.enecuumwallet.androidapp.ui.adapters.TokenSendListAdapter
import com.enecuumwallet.androidapp.utils.KeyboardUtils
import com.enecuumwallet.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_tokens_single.*

class TokensSingleFragment : MvpAppCompatFragment(), TokensSingleView {
    companion object {
        const val TAG = "TokensSingleFragment"
        const val MODE = "mode"
        enum class Mode {
            TokenMode,
            JettonMode
        }
        fun newInstance(mode: Mode): TokensSingleFragment {
            val fragment: TokensSingleFragment = TokensSingleFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            args.putSerializable(MODE, mode)
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: TokensSinglePresenter
    private var adapter : TokenSendListAdapter? = null
    private lateinit var currentMode: Mode

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokens_single, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.handleArgs(arguments)
        searchField.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.hideKeyboard(context, searchField)
            }
            true
        }
        searchField.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                adapter?.filter(s.toString())
            }
        })
        send.setOnClickListener {
            presenter.onSendClick()
        }
        receive.setOnClickListener {
            presenter.onReceiveClick()
        }
    }

    override fun setupWithMode(currentMode: Mode, listOf: List<TokenInfo>) {
        this.currentMode = currentMode
        if(currentMode == Mode.JettonMode) {
            send.visibility = View.GONE
            receive.visibility = View.GONE
        }
        tokensList.layoutManager = LinearLayoutManager(context)
        adapter = TokenSendListAdapter(listOf, currentMode)
        tokensList.adapter = adapter
    }

    override fun changeButtonState(enable: Boolean) {
        send.isEnabled = enable
        receive.isEnabled = enable
    }
}
