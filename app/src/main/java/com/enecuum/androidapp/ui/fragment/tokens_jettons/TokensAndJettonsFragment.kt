package com.enecuum.androidapp.ui.fragment.tokens_jettons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.base_ui_primitives.BackTitleFragment
import com.enecuum.androidapp.base_ui_primitives.OrdinalTitleFragment
import com.enecuum.androidapp.presentation.presenter.tokens_jettons.TokensAndJettonsPresenter
import com.enecuum.androidapp.presentation.view.tokens_jettons.TokensAndJettonsView

class TokensAndJettonsFragment : BackTitleFragment(), TokensAndJettonsView {
    companion object {
        const val TAG = "TokensAndJettonsFragment"

        fun newInstance(): TokensAndJettonsFragment {
            val fragment: TokensAndJettonsFragment = TokensAndJettonsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: TokensAndJettonsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokens_and_jettons, container, false)
    }

    override fun getTitle(): String = getString(R.string.tokens_and_jettons)
}
