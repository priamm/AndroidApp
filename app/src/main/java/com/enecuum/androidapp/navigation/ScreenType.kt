package com.enecuum.androidapp.navigation

import com.enecuum.androidapp.ui.activity.forgot.ForgotPinActivity
import com.enecuum.androidapp.ui.activity.main.MainActivity
import com.enecuum.androidapp.ui.activity.new_account.NewAccountActivity
import com.enecuum.androidapp.ui.activity.registration.RegistrationActivity
import com.enecuum.androidapp.ui.activity.registration_finished.RegistrationFinishedActivity
import com.enecuum.androidapp.ui.activity.restore_pin.RestorePinActivity
import com.enecuum.androidapp.ui.activity.signin.SignInActivity
import com.enecuum.androidapp.ui.activity.splash.SplashActivity
import kotlin.reflect.KClass

/**
 * Created by oleg on 22.01.18.
 */
enum class ScreenType {
    Splash,
    Registration,
    NewAccount,
    RegistrationFinished,
    SignIn,
    ForgotPin,
    RestorePin,
    Main,
    Mining,
    QrCode,
    Terms
}

enum class FragmentType {
    Home,
    Send,
    Receive,
    History,
    Tokens
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
        ScreenType.NewAccount -> {
            NewAccountActivity::class.java
        }
        ScreenType.SignIn -> {
            SignInActivity::class.java
        }
        ScreenType.RegistrationFinished -> {
            RegistrationFinishedActivity::class.java
        }
        ScreenType.ForgotPin -> {
            ForgotPinActivity::class.java
        }
        ScreenType.RestorePin -> {
            RestorePinActivity::class.java
        }
        else -> null
    }
}