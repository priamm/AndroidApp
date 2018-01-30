package com.enecuum.androidapp.base_ui_primitives.tab_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentNavigator
import com.enecuum.androidapp.navigation.FragmentType
import kotlin.reflect.KClass

/**
 * Created by oleg on 29.01.18.
 */
open class BaseTabFragment : Fragment() {
    private var navigator: FragmentNavigator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onResume() {
        super.onResume()
        if(navigator == null) {
            navigator = FragmentNavigator(activity, childFragmentManager, R.id.fragmentContainer)
        }
        EnecuumApplication.fragmentCicerone().navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        EnecuumApplication.fragmentCicerone().navigatorHolder.removeNavigator()
    }
}