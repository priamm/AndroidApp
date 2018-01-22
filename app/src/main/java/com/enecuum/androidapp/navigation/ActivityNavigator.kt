package com.enecuum.androidapp.navigation

import android.app.Activity
import android.content.Intent
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
                }
            }
        }
    }

    private fun generateScreenIntent(screenKey : String): Intent {
        val screenType = ScreenType.valueOf(screenKey)
        return Intent(currentActivity, ScreenClass.getClassBy(screenType))
    }
}