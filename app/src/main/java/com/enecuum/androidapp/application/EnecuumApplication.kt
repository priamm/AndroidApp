package com.enecuum.androidapp.application

import android.app.Application
import android.content.Context
import com.enecuum.androidapp.events.MainActivityStopped
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.ScreenType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.utils.EventBusUtils
import com.google.crypto.tink.Config
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.signature.SignatureKeyTemplates
import com.jraska.console.timber.ConsoleTree
import org.greenrobot.eventbus.Subscribe
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import timber.log.Timber


/**
 * Created by oleg on 22.01.18.
 */
class EnecuumApplication : Application() {

    companion object {
        private lateinit var appContext: Context
        fun applicationContext(): Context = appContext

        private lateinit var cicerone: Cicerone<Router>
        fun cicerone(): Cicerone<Router> = cicerone

        private lateinit var fragmentCicerone: Cicerone<Router>
        fun fragmentCicerone(): Cicerone<Router> = fragmentCicerone

        private lateinit var tabCicerone: Cicerone<Router>
        fun tabCicerone(): Cicerone<Router> = tabCicerone

        private var currentTab = TabType.Home
        private val backStack = mutableMapOf<TabType, Int>()

        private lateinit var keysetManager: AndroidKeysetManager
        fun keysetManager(): AndroidKeysetManager = keysetManager

        fun navigateToActivity(screenType: ScreenType, transitionData: Any? = null) {
            if (screenType == ScreenType.Main) {
                cicerone.router.newRootScreen(screenType.toString(), transitionData)
                return
            }
            cicerone.router.navigateTo(screenType.toString(), transitionData)
        }

        fun navigateToFragment(screenType: FragmentType, tabType: TabType, transitionData: Any? = null) {
            currentTab = tabType
            if (backStack.containsKey(tabType)) {
                var newVal = backStack[tabType]!!
                newVal++
                backStack[tabType] = newVal
            } else {
                backStack[tabType] = 1
            }
            if (isRoot(screenType, tabType))
                newRootScreen(screenType, transitionData)
            fragmentCicerone.router.navigateTo(screenType.toString(), transitionData)
        }

        private fun isRoot(screenType: FragmentType, tabType: TabType): Boolean {
            if (screenType == FragmentType.Balance && tabType == TabType.Home) {
                return true
            }
            if (screenType == FragmentType.SendOptions && tabType == TabType.Send) {
                return true
            }
            if (screenType == FragmentType.ReceiveByAddress && tabType == TabType.Receive) {
                return true
            }
            if (screenType == FragmentType.SettingsMain && tabType == TabType.Settings) {
                return true
            }
            return false
        }

        private fun newRootScreen(screenType: FragmentType, transitionData: Any?) {
            fragmentCicerone.router.newRootScreen(screenType.toString(), transitionData)
        }

        fun exitFromCurrentFragment() {
            if (backStack.containsKey(currentTab)) {
                var newVal = backStack[currentTab]!!
                newVal--
                if (newVal < 0)
                    newVal = 0
                backStack[currentTab] = newVal
            }
            fragmentCicerone.router.exit()
        }


        fun navigateToTab(tabType: TabType) {
            currentTab = tabType
            tabCicerone.router.navigateTo(tabType.toString())
        }

        fun getCurrentBackStackCount(): Int {
            if (backStack.containsKey(currentTab))
                return backStack[currentTab]!!
            return 0
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Config.register(TinkConfig.TINK_1_1_0);

        keysetManager = AndroidKeysetManager.Builder()
                .withSharedPref(appContext, "my_keyset_name", "my_pref_file_name")
                .withMasterKeyUri("android-keystore://my_master_key_id")
                .withKeyTemplate(SignatureKeyTemplates.ECDSA_P256)
                .build()

        Timber.plant(ConsoleTree());
        Timber.plant()

        cicerone = Cicerone.create()
        fragmentCicerone = Cicerone.create()
        tabCicerone = Cicerone.create()

        EventBusUtils.register(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        EventBusUtils.unregister(this)
    }

    @Subscribe
    fun onMainActivityStopped(event: MainActivityStopped) {
        backStack.clear()
    }

}