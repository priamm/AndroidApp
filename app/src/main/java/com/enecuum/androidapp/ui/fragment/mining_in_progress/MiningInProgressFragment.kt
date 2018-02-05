package com.enecuum.androidapp.ui.fragment.mining_in_progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.mining_in_progress.MiningInProgressView
import com.enecuum.androidapp.presentation.presenter.mining_in_progress.MiningInProgressPresenter

import com.arellomobile.mvp.presenter.InjectPresenter

class MiningInProgressFragment : MvpAppCompatFragment(), MiningInProgressView {
    companion object {
        const val TAG = "MiningInProgressFragment"

        fun newInstance(): MiningInProgressFragment {
            val fragment: MiningInProgressFragment = MiningInProgressFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: MiningInProgressPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mining_in_progress, container, false)
    }

}
