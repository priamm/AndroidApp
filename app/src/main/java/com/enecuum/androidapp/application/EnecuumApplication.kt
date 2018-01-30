package com.enecuum.androidapp.application

import android.app.Application
import android.content.Context
import com.enecuum.androidapp.events.MainActivityStopped
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.navigation.TabType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

/**
 * Created by oleg on 22.01.18.
 */
class EnecuumApplication : Application() {
    companion object {
        private lateinit var appContext: Context
        fun applicationContext() : Context = appContext

        private lateinit var cicerone: Cicerone<Router>
        fun cicerone() : Cicerone<Router> = cicerone

        private lateinit var fragmentCicerone: Cicerone<Router>
        fun fragmentCicerone() : Cicerone<Router> = fragmentCicerone

        private lateinit var tabCicerone: Cicerone<Router>
        fun tabCicerone() : Cicerone<Router> = tabCicerone

        private var currentTab = TabType.Home
        private val backStack = mutableMapOf<TabType, Int>()

        fun navigateToActivity(screenType: ScreenType) {
            if(screenType == ScreenType.Main) {
                cicerone.router.newRootScreen(screenType.toString())
                return
            }
            cicerone.router.navigateTo(screenType.toString())
        }

        fun navigateToFragment(screenType: FragmentType, tabType: TabType) {
            currentTab = tabType
            if(backStack.containsKey(tabType)) {
                var newVal = backStack[tabType]!!
                newVal++
                backStack[tabType] = newVal
            } else {
                backStack[tabType] = 1
            }
            if(screenType == FragmentType.Balance) {
                fragmentCicerone.router.newRootScreen(screenType.toString())
                return
            }
            if(screenType == FragmentType.SendOptions) {
                fragmentCicerone.router.newRootScreen(screenType.toString())
                return
            }
            fragmentCicerone.router.navigateTo(screenType.toString())
        }

        fun exitFromCurrentFragment() {
            if(backStack.containsKey(currentTab)) {
                var newVal = backStack[currentTab]!!
                newVal--
                if(newVal < 0)
                    newVal = 0
                backStack[currentTab] = newVal
            }
            fragmentCicerone.router.exit()
        }


        fun navigateToTab(tabType: TabType) {
            currentTab = tabType
            tabCicerone.router.navigateTo(tabType.toString())
        }

        fun getCurrentBackStackCount() : Int {
            if(backStack.containsKey(currentTab))
                return backStack[currentTab]!!
            return 0
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        cicerone = Cicerone.create()
        fragmentCicerone = Cicerone.create()
        tabCicerone = Cicerone.create()
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onMainActivityStopped(event: MainActivityStopped) {
        backStack.clear()
    }

}