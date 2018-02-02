package com.enecuum.androidapp.persistent_data

import android.content.Context
import android.content.SharedPreferences
import com.enecuum.androidapp.BuildConfig
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.Currency

/**
 * Created by oleg on 22.01.18.
 */
object PersistentStorage {
    private const val IS_REGISTRATION_FINISHED = "IS_REGISTRATION_FINISHED"
    private const val KEY_PATH = "KEY_PATH"
    private const val PIN = "PIN"
    private const val ENQ_AMOUNT = "ENQ_AMOUNT"
    private const val ENQ_PLUS_AMOUNT = "ENQ_PLUS_AMOUNT"
    private const val TOKEN_AMOUNT = "TOKEN_AMOUNT"
    private const val JETTON_AMOUNT = "JETTON_AMOUNT"
    private const val ADDRESS = "ADDRESS"

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

    private fun setFloat(key: String, value: Float) {
        val editor = getPrefs().edit()
        editor.putFloat(key, value)
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

    fun getPin() : String = getPrefs().getString(PIN, "")

    fun setPin(pin : String) {
        if(pin.isEmpty())
            return
        setString(PIN, pin)
    }

    fun deletePin() {
        setString(PIN,"")
    }

    fun getCurrencyAmount(currency: Currency): Float = when(currency) {
        //TODO: fill with 0 default
        Currency.Enq -> getPrefs().getFloat(ENQ_AMOUNT, 1000f)
        Currency.EnqPlus -> getPrefs().getFloat(ENQ_PLUS_AMOUNT, 2000f)
        Currency.Token -> getPrefs().getFloat(TOKEN_AMOUNT, 3000f)
        Currency.Jetton -> getPrefs().getFloat(JETTON_AMOUNT, 4000f)
    }

    fun setCurrencyAmount(currency: Currency, amount: Float) {
        when(currency) {
            Currency.Enq -> setFloat(ENQ_AMOUNT, amount)
            Currency.EnqPlus -> setFloat(ENQ_PLUS_AMOUNT, amount)
            Currency.Token -> setFloat(TOKEN_AMOUNT, amount)
            Currency.Jetton -> setFloat(JETTON_AMOUNT, amount)
        }
    }

    fun getAddress() : String = getPrefs().getString(ADDRESS, "5Kb8kLL6TsZZY36hWXMssSzNydYXYB9")

    fun setAddress(address : String) {
        if(address.isEmpty())
            return
        setString(ADDRESS, address)
    }

    fun deleteAddress() {
        val editor = getPrefs().edit()
        editor.remove(ADDRESS)
        editor.apply()
    }
}