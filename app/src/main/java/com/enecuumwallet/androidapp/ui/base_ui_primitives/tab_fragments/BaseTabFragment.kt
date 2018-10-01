package com.enecuumwallet.androidapp.ui.base_ui_primitives.tab_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.events.MainActivityStopped
import com.enecuumwallet.androidapp.navigation.FragmentNavigator
import com.enecuumwallet.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.terrakok.cicerone.android.SupportAppNavigator

/**
 * Created by oleg on 29.01.18.
 */
open class BaseTabFragment : Fragment() {
    private var navigator: SupportAppNavigator? = null
    protected var isSetupFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtils.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    @Subscribe
    fun onMainActivityStopped(event: MainActivityStopped) {
        isSetupFinished = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = FragmentNavigator(activity, childFragmentManager, R.id.fragmentContainer)

        EnecuumApplication.fragmentCicerone().navigatorHolder.setNavigator(navigator)
    }

    fun getNavigator(): SupportAppNavigator? {
       return navigator
    }

    override fun onDestroyView() {
        EnecuumApplication.fragmentCicerone().navigatorHolder.removeNavigator()
        super.onDestroyView()
    }
}