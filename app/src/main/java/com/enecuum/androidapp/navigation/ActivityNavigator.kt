package com.enecuum.androidapp.navigation

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
import android.widget.Toast
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.*

/**
 * Created by oleg on 22.01.18.
 */
class ActivityNavigator(private val currentActivity : Activity?) : Navigator {
    override fun applyCommands(commands: Array<out Command>?) {
        if(commands != null) {
            for(command in commands) {
                when(command) {
                    is Back -> {
                        currentActivity?.finish()
                    }
                    is Forward -> {
                        val intent = generateScreenIntent(command.screenKey)
                        if(command.transitionData != null)
                            intent.putExtra(TransitionData.Key, command.transitionData as TransitionData)
                        currentActivity?.startActivity(intent)
                    }
                    is BackTo -> {
                        currentActivity?.navigateUpTo(generateScreenIntent(command.screenKey))
                    }
                    is Replace -> {
                        val intent = generateScreenIntent(command.screenKey)
                        intent.putExtra(TransitionData.Key, command.transitionData as TransitionData)
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
    private val period = 500L

    private fun displayMessage(text: String) {
        if(currentActivity == null)
            return
        if(isDisplayingMessage)
            return
        synchronized(locker, {
            isDisplayingMessage = true
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                val snackbar = Snackbar.make(currentActivity.window!!.decorView, text, Snackbar.LENGTH_LONG)
                snackbar.show()
                isDisplayingMessage = false
            }, period)
        })
    }

    private fun generateScreenIntent(screenKey : String): Intent {
        val screenType = ScreenType.valueOf(screenKey)
        return Intent(currentActivity, ScreenClass.getClassBy(screenType))
    }
}