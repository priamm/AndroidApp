package com.enecuum.androidapp.base_ui_primitives

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.navigation.FragmentNavigator
import com.enecuum.androidapp.navigation.FragmentType
import kotlinx.android.synthetic.main.fragment_container.*

/**
 * Created by oleg on 29.01.18.
 */
open class BaseTabFragment : Fragment() {
    protected var isSetupFinished = false
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

class HomeTab : BaseTabFragment() {
    override fun onResume() {
        super.onResume()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if(!isSetupFinished) {
                isSetupFinished = true
                EnecuumApplication.navigateToFragment(FragmentType.Balance)
            }
        }, 300)
    }
}
class ReceiveTab : BaseTabFragment()
class SendTab : BaseTabFragment()
class SettingsTab : BaseTabFragment()