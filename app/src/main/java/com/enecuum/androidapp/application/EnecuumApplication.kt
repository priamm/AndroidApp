package com.enecuum.androidapp.application

import android.app.Application
import android.content.Context
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.navigation.TabType
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

        fun navigateToActivity(screenType: ScreenType) {
            if(screenType == ScreenType.Main) {
                cicerone.router.newRootScreen(screenType.toString())
                return
            }
            cicerone.router.navigateTo(screenType.toString())
        }

        fun navigateToFragment(screenType: FragmentType) {
            if(screenType == FragmentType.Balance) {
                fragmentCicerone.router.newRootScreen(screenType.toString())
                return
            }
            fragmentCicerone.router.navigateTo(screenType.toString())
        }

        fun navigateToTab(tabType: TabType) {
            tabCicerone.router.navigateTo(tabType.toString())
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        cicerone = Cicerone.create()
        fragmentCicerone = Cicerone.create()
        tabCicerone = Cicerone.create()
    }

}