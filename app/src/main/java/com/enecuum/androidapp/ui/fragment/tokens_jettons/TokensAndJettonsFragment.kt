package com.enecuum.androidapp.ui.fragment.tokens_jettons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.ui.base_ui_primitives.BackTitleFragment
import com.enecuum.androidapp.presentation.presenter.tokens_jettons.TokensAndJettonsPresenter
import com.enecuum.androidapp.presentation.view.tokens_jettons.TokensAndJettonsView
import com.enecuum.androidapp.ui.adapters.SendTokensTabsAdapter
import kotlinx.android.synthetic.main.fragment_tokens_and_jettons.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = SendTokensTabsAdapter(childFragmentManager, context!!)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun getTitle(): String = getString(R.string.tokens_and_jettons)
}
