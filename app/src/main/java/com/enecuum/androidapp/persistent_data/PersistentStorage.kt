package com.enecuum.androidapp.persistent_data

import android.content.Context
import android.content.SharedPreferences
import com.enecuum.androidapp.BuildConfig
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.Currency

private val i = 3

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
    private const val IS_MINING_IN_PROGRESS = "IS_MINING_IN_PROGRESS"
    private const val COUNT_TRANSACTIONS = "COUNT_TRANSACTIONS"
    private const val CURRENT_NN = "CURRENT_NN"
    private const val AUTO_MINING_START = "AUTO_MINING_START"
    private fun getPrefs(): SharedPreferences = EnecuumApplication.applicationContext()
            .getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    private fun setBoolean(key: String, value: Boolean) {
        val editor = getPrefs().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getBoolean(key: String): Boolean {
        return getPrefs().getBoolean(key, false)
    }

    private fun setString(key: String, value: String) {
        val editor = getPrefs().edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun setInt(key: String, value: Int) {
        val editor = getPrefs().edit()
        editor.putInt(key, value)
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

    fun isRegistrationFinished(): Boolean =
            getPrefs().getBoolean(IS_REGISTRATION_FINISHED, false)

    fun getKeyPath(): String = getPrefs().getString(KEY_PATH, "")

    fun setKeyPath(keyPath: String) {
        setString(KEY_PATH, keyPath)
    }

    fun getPin(): String = getPrefs().getString(PIN, "")

    fun setPin(pin: String) {
        if (pin.isEmpty())
            return
        setString(PIN, pin)
    }

    fun deletePin() {
        setString(PIN, "")
    }

    fun getCurrencyAmount(currency: Currency): Float = when (currency) {
        //TODO: fill with 0 default
        Currency.Enq -> getPrefs().getFloat(ENQ_AMOUNT, 1000f)
        Currency.EnqPlus -> getPrefs().getFloat(ENQ_PLUS_AMOUNT, 2000f)
        Currency.Token -> getPrefs().getFloat(TOKEN_AMOUNT, 3000f)
        Currency.Jetton -> getPrefs().getFloat(JETTON_AMOUNT, 4000f)
    }

    fun setCurrencyAmount(currency: Currency, amount: Float) {
        when (currency) {
            Currency.Enq -> setFloat(ENQ_AMOUNT, amount)
            Currency.EnqPlus -> setFloat(ENQ_PLUS_AMOUNT, amount)
            Currency.Token -> setFloat(TOKEN_AMOUNT, amount)
            Currency.Jetton -> setFloat(JETTON_AMOUNT, amount)
        }
    }

    fun getAddress(): String = getPrefs().getString(ADDRESS, "")

    fun setAddress(address: String) {
        if (address.isEmpty())
            return
        setString(ADDRESS, address)
    }

    fun deleteAddress() {
        val editor = getPrefs().edit()
        editor.remove(ADDRESS)
        editor.apply()
    }

    fun setMiningInProgress(value: Boolean) {
        setBoolean(IS_MINING_IN_PROGRESS, value)
    }

    fun isMiningInProgress(): Boolean =
            getPrefs().getBoolean(IS_MINING_IN_PROGRESS, false)


    fun getCurrentNNAddress(): String = getPrefs().getString(CURRENT_NN, "")

    fun setCurrentNNAddress(address: String) {
        if (address.isEmpty())
            return
        setString(CURRENT_NN, address)
    }

    private val DEFAULT_TRANSACTIONS_COUNT = 250

    fun getCountTransactionForRequest(): Int = getPrefs().getInt(COUNT_TRANSACTIONS, DEFAULT_TRANSACTIONS_COUNT)
    fun setCountTransactionForRequest(value: Int) {
        setInt(COUNT_TRANSACTIONS, value)
    }

    fun setAutoMiningStart(autoStart: Boolean) {
        setBoolean(AUTO_MINING_START, autoStart)
    }

    fun getAutoMiningStart(): Boolean {
        return getBoolean(AUTO_MINING_START)
    }
}