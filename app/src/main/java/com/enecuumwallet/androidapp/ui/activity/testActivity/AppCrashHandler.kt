package com.enecuumwallet.androidapp.ui.activity.testActivity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.crashlytics.android.Crashlytics

import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.ui.activity.splash.SplashActivity

class AppCrashHandler(private val activity: Activity) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        Crashlytics.log("uncaughtException, app will be restarted")
        Crashlytics.logException(ex)

        val intent = Intent(activity, SplashActivity::class.java)
        intent.putExtra("crash", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(EnecuumApplication.applicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val mgr = EnecuumApplication.applicationContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
        activity.finish()
        System.exit(2)
    }
}