package com.enecuum.androidapp.persistent_data

import android.content.Context
import android.content.SharedPreferences
import com.enecuum.androidapp.BuildConfig
import com.enecuum.androidapp.application.EnecuumApplication

/**
 * Created by oleg on 22.01.18.
 */
object PersistentStorage {
    private const val IS_REGISTRATION_FINISHED = "IS_REGISTRATION_FINISHED"
    private const val KEY_PATH = "KEY_PATH"

    private fun getPrefs() : SharedPreferences = EnecuumApplication.applicationContext()
            .getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    private fun setBoolean(key: String, value: Boolean) {
        val editor = getPrefs().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun setString(key: String, value: String) {
        val editor = getPrefs().edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setRegistrationFinished() {
        setBoolean(IS_REGISTRATION_FINISHED, true)
    }

    fun isRegistrationFinished() : Boolean =
            getPrefs().getBoolean(IS_REGISTRATION_FINISHED, false)

    fun getKeyPath() : String = getPrefs().getString(KEY_PATH, "")

    fun setKeyPath(keyPath: String) {
        setString(KEY_PATH, keyPath)
    }
}