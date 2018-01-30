package com.enecuum.androidapp.ui.fragment.send_parameters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.presenter.send_parameters.SendParametersPresenter
import com.enecuum.androidapp.presentation.view.send_parameters.SendParametersView
import com.enecuum.androidapp.ui.base_ui_primitives.NoBackFragment

class SendParametersFragment : NoBackFragment(), SendParametersView {

    companion object {
        const val TAG = "SendParametersFragment"

        fun newInstance(): SendParametersFragment {
            val fragment: SendParametersFragment = SendParametersFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: SendParametersPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_parameters, container, false)
    }

    override fun getTitle(): String = getString(R.string.send)
}
