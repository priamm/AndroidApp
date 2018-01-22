package com.enecuum.androidapp.navigation

import com.enecuum.androidapp.ui.activity.main.MainActivity
import com.enecuum.androidapp.ui.activity.registration.RegistrationActivity
import com.enecuum.androidapp.ui.activity.splash.SplashActivity
import kotlin.reflect.KClass

/**
 * Created by oleg on 22.01.18.
 */
enum class ScreenType {
    Splash,
    Registration,
    CreatePin,
    SignIn,
    Main,
    Mining,
    QrCode,
    Terms
}

enum class TabType {
    Home,
    Send,
    Receive,
    History
}

object ScreenClass {
    fun getClassBy(screenType: ScreenType) : Class<*>? = when(screenType) {
        ScreenType.Splash -> {
            SplashActivity::class.java
        }
        ScreenType.Main -> {
            MainActivity::class.java
        }
        ScreenType.Registration -> {
            RegistrationActivity::class.java
        }
        else -> null
    }
}