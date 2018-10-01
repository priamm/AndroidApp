package com.enecuumwallet.androidapp.ui.fragment.loading

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuumwallet.androidapp.R
import kotlinx.android.synthetic.main.fragment_loading.*

open class LoadingFragment : MvpAppCompatFragment() {
    companion object {
        const val TAG = "LoadingFragment"

        fun newInstance(): LoadingFragment {
            val fragment: LoadingFragment = LoadingFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.rotate))
    }
}
