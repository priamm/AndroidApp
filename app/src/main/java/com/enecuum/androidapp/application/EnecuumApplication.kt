package com.enecuum.androidapp.application

import android.app.Application
import android.content.Context
import com.enecuum.androidapp.navigation.ScreenType
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

        fun navigateTo(screenType: ScreenType) {
            cicerone.router.navigateTo(screenType.toString())
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        cicerone = Cicerone.create()
    }

}