package com.enecuum.androidapp.ui.fragment.new_account_qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.new_account_qr.NewAccountQrView
import com.enecuum.androidapp.presentation.presenter.new_account_qr.NewAccountQrPresenter
import com.arellomobile.mvp.presenter.InjectPresenter

class NewAccountQrFragment : MvpAppCompatFragment(), NewAccountQrView {
    companion object {
        const val TAG = "NewAccountQrFragment"

        fun newInstance(): NewAccountQrFragment {
            val fragment: NewAccountQrFragment = NewAccountQrFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mNewAccountQrPresenter: NewAccountQrPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_account_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
