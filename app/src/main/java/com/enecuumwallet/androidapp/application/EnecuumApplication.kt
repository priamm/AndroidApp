package com.enecuumwallet.androidapp.application

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.enecuumwallet.androidapp.application.Constants.AppConstants.BASE_URL_OTP
import com.enecuumwallet.androidapp.events.MainActivityStopped
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.network.api.OtpApi
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.ui.activity.testActivity.AppCrashHandler
import com.enecuumwallet.androidapp.utils.EventBusUtils
import com.google.crypto.tink.Config
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.signature.SignatureKeyTemplates
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jraska.console.timber.ConsoleTree
import io.fabric.sdk.android.Fabric
import org.greenrobot.eventbus.Subscribe
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import timber.log.Timber
import java.util.*
import com.jraska.console.Console
import com.squareup.leakcanary.LeakCanary
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by oleg on 22.01.18.
 */
class EnecuumApplication : MultiDexApplication() {


    companion object {
        private lateinit var appContext: Context
        fun applicationContext(): Context = appContext

        private lateinit var cicerone: Cicerone<Router>
        fun cicerone(): Cicerone<Router> = cicerone

        private lateinit var fragmentCicerone: Cicerone<Router>
        fun fragmentCicerone(): Cicerone<Router> = fragmentCicerone

        private lateinit var tabCicerone: Cicerone<Router>
        fun tabCicerone(): Cicerone<Router> = tabCicerone

        private var currentTab = TabType.Balance
        private val backStack = mutableMapOf<TabType, Int>()

        lateinit var otpApi : OtpApi
            private set

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
            if (screenType == FragmentType.Balance && tabType == TabType.Balance) {
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



        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)

        appContext = applicationContext

        otpApi = providesOtpApi()

        Config.register(TinkConfig.TINK_1_1_0)

//        Security.addProvider(BouncyCastleProvider())
//        keysetManager = AndroidKeysetManager.Builder()
//                .withSharedPref(appContext, "my_keyset_name", "my_pref_file_name")
//                .withMasterKeyUri("android-keystore://my_master_key_id")
//                .withKeyTemplate(SignatureKeyTemplates.ECDSA_P256)
//                .build()


        Timber.plant(Timber.DebugTree())
        Timber.plant()

        cicerone = Cicerone.create()
        fragmentCicerone = Cicerone.create()
        tabCicerone = Cicerone.create()

        EventBusUtils.register(this)

        //run clean log timer
        val timer = Timer()
        timer.schedule(CleanTimberTask(), 60000)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                Thread.setDefaultUncaughtExceptionHandler(AppCrashHandler(activity!!))
            }

        })

        Fabric.with(this, Crashlytics())
    }

    override fun onTerminate() {
        super.onTerminate()
        EventBusUtils.unregister(this)
    }

    @Subscribe
    fun onMainActivityStopped(event: MainActivityStopped) {
        backStack.clear()
    }

    internal inner class CleanTimberTask : TimerTask() {
        override fun run() {
            Console.clear()
        }
    }

    private fun providesRetrofit() : Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL_OTP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun providesOtpApi() : OtpApi {
        return providesRetrofit().create( OtpApi::class.java )
    }


}