package com.enecuum.androidapp.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.widget.TextView
import android.widget.Toast
import com.enecuum.androidapp.R
import com.enecuum.androidapp.ui.activity.change_pin.ChangePinActivity
import com.enecuum.androidapp.ui.activity.forgot.ForgotPinActivity
import com.enecuum.androidapp.ui.activity.main.MainActivity
import com.enecuum.androidapp.ui.activity.mining.MiningActivity
import com.enecuum.androidapp.ui.activity.new_account.NewAccountActivity
import com.enecuum.androidapp.ui.activity.registration.RegistrationActivity
import com.enecuum.androidapp.ui.activity.registration_finished.RegistrationFinishedActivity
import com.enecuum.androidapp.ui.activity.scan.ScanActivity
import com.enecuum.androidapp.ui.activity.signin.SignInActivity
import com.enecuum.androidapp.ui.activity.splash.SplashActivity
import com.enecuum.androidapp.ui.activity.transaction_details.TransactionDetailsActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.*

/**
 * Created by oleg on 22.01.18.
 */
class ActivityNavigator(private val currentActivity : Activity?) : Navigator {
    companion object {
        private fun generateScreenIntent(currentActivity: Activity?, screenKey : String, data : Any? = null): Intent? {
            try {
                val screenType = ScreenType.valueOf(screenKey)
                val intent = Intent(currentActivity, getClassBy(screenType))
                if(data != null)
                    intent.putExtras(data as Bundle)
                return intent
            } catch (e : Throwable) {
                e.printStackTrace()
            }
            return null
        }

        private fun getClassBy(screenType: ScreenType) : Class<*>? = when(screenType) {
            ScreenType.Splash -> SplashActivity::class.java
            ScreenType.Main -> MainActivity::class.java
            ScreenType.Registration -> RegistrationActivity::class.java
            ScreenType.NewAccount -> NewAccountActivity::class.java
            ScreenType.SignIn -> SignInActivity::class.java
            ScreenType.RegistrationFinished -> RegistrationFinishedActivity::class.java
            ScreenType.ForgotPin -> ForgotPinActivity::class.java
            ScreenType.ChangePin -> ChangePinActivity::class.java
            ScreenType.Scan -> ScanActivity::class.java
            ScreenType.Mining -> MiningActivity::class.java
            ScreenType.TransactionDetails -> TransactionDetailsActivity::class.java
        }
    }
    private var isPendingClearStackIntent = false

    override fun applyCommands(commands: Array<out Command>?) {
        if(commands != null) {
            for(command in commands) {
                when(command) {
                    is Back -> {
                        currentActivity?.finish()
                    }
                    is Forward -> {
                        val intent = generateScreenIntent(currentActivity, command.screenKey, command.transitionData)
                        currentActivity?.startActivity(intent)
                    }
                    is BackTo -> {
                        if(command.screenKey != null) {
                            currentActivity?.navigateUpTo(generateScreenIntent(currentActivity, command.screenKey))
                        } else {
                            isPendingClearStackIntent = true
                        }
                    }
                    is Replace -> {
                        val intent = generateScreenIntent(currentActivity, command.screenKey, command.transitionData)
                        if(isPendingClearStackIntent) {
                            isPendingClearStackIntent = false
                            intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        currentActivity?.finish()
                        currentActivity?.startActivity(intent)
                    }
                    is SystemMessage -> {
                        displayMessage(command.message)
                    }

                }
            }
        }
    }

    private var isDisplayingMessage = false
    private val locker = Any()
    private val period = 2000L

    private fun displayMessage(text: String) {
        if(currentActivity == null)
            return
        if(isDisplayingMessage)
            return
        synchronized(locker, {
            isDisplayingMessage = true
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val toast = Toast.makeText(currentActivity, text, Toast.LENGTH_SHORT)
                val view = toast.view
                view.setBackgroundResource(R.drawable.toast_bg)
                val message = view.findViewById<TextView>(android.R.id.message)
                message.setTextColor(ContextCompat.getColor(currentActivity, android.R.color.white))
                toast.show()
            }
            handler.postDelayed({
                isDisplayingMessage = false
            }, period)
        })
    }
}