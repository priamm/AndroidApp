package com.enecuum.androidapp.ui.fragment.mining_start

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.mining_start.MiningStartView
import com.enecuum.androidapp.presentation.presenter.mining_start.MiningStartPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_mining_start.*

class MiningStartFragment : MvpAppCompatFragment(), MiningStartView {
    companion object {
        const val TAG = "MiningStartFragment"

        fun newInstance(): MiningStartFragment {
            val fragment: MiningStartFragment = MiningStartFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: MiningStartPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mining_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start.setOnClickListener {
            presenter.onStartClick()
        }
        val supportActivity = activity as AppCompatActivity?
        supportActivity?.supportActionBar?.setBackgroundDrawable(null)
    }
}
