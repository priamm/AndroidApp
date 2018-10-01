package com.enecuumwallet.androidapp.ui.base_ui_primitives

import android.os.Handler
import android.os.Looper

/**
 * Created by oleg on 23.01.18.
 */
abstract class SlideshowTitleFragment : TitleFragment() {
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            if(activity != null)
                activity?.title = getTitle()
            else {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if(getTitle().isNotEmpty())
                        activity?.title = getTitle()
                }, 300)
            }

        }
    }
}