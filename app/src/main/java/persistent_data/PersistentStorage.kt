package persistent_data

import android.content.Context
import android.content.SharedPreferences
import com.enecuum.androidapp.BuildConfig

/**
 * Created by oleg on 22.01.18.
 */
object PersistentStorage {
    private const val IS_REGISTRATION_FINISHED = "IS_REGISTRATION_FINISHED"

    private fun getPrefs() : SharedPreferences = application.EnecuumApplication.applicationContext()
            .getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun setRegistrationFinished() {
        val editor = getPrefs().edit()
        editor.putBoolean(IS_REGISTRATION_FINISHED, true)
        editor.apply()
    }

    fun isRegistrationFinished() : Boolean =
            getPrefs().getBoolean(IS_REGISTRATION_FINISHED, false)

}