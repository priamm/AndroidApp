package com.enecuum.androidapp.persistent_data


import android.content.Context
import android.provider.Settings
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.Options
import com.yakivmospan.scytale.Store
import java.io.IOException
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException

class SecurityManager(context: Context) {
    private val store: Store
    private val androidId: String
    private val accessPreferences: AccessPreferences
    private val KEYSTORE_NAME = "KEYSTORE_NAME"
    private val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)

    init {
        androidId = Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID) + context.applicationContext.packageName
        store = Store(context, KEYSTORE_NAME, androidId.toCharArray())
        val sharedPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        accessPreferences = AccessPreferences(sharedPref)
    }

    private fun putValue(key: String, data: String) {
        val secretKey = store.generateSymmetricKey(key, data.toCharArray())
        val encrypt = crypto.encrypt(data, secretKey)
        accessPreferences.put<Any>(key, encrypt)
    }


    fun getString(key: String, defValue: String?): String? {
        return getValue(key, defValue)
    }

    fun getValue(key: String, returnIfNull: String?): String? {

        if (!contains(key)) {
            accessPreferences.remove(key)
            remove(key)
            return returnIfNull
        }

        val value = accessPreferences[key, ""] ?: return returnIfNull

        val symmetricKey = store.getSymmetricKey(key, androidId.toCharArray())
                ?: return returnIfNull

        return crypto.decrypt(value, symmetricKey)
    }

    fun getInt(key: String, defValue: Int): Int {
        val string = getValue(key, defValue.toString())
        return Integer.parseInt(string)
    }

    fun getLong(key: String, defValue: Long): Long {
        val string = getValue(key, defValue.toString())
        return java.lang.Long.parseLong(string)
    }

    fun getFloat(key: String, defValue: Float): Float {
        val string = getValue(key, defValue.toString())
        return java.lang.Float.parseFloat(string)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val string = getValue(key, defValue.toString())
        return java.lang.Boolean.parseBoolean(string)
    }

    operator fun contains(key: String): Boolean {
        return accessPreferences.contains(key) && store.hasKey(key)
    }

    fun putString(key: String, value: String?) {
        putValue(key, value.toString())
    }

    fun putByteArray(key: String, value: ByteArray) {
        putString(key, String(value, Charset.forName("UTF-8")))
    }

    fun putInt(key: String, value: Int) {
        putValue(key, value.toString())
    }


    fun putLong(key: String, value: Long) {
        putValue(key, value.toString())
    }


    fun putFloat(key: String, value: Float) {
        putValue(key, value.toString())

    }


    fun putBoolean(key: String, value: Boolean) {
        putValue(key, value.toString())
    }


    fun remove(key: String): SecurityManager {
        store.deleteKey(key)
        accessPreferences.remove(key)
        return this
    }

    fun clean() {
        var ks: KeyStore? = null
        try {
            ks = KeyStore.getInstance(KEYSTORE_NAME)
            try {
                ks!!.load(null)
                val aliases = ks.aliases()
                while (aliases.hasMoreElements()) {
                    val alias = aliases.nextElement()
                    remove(alias)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: CertificateException) {
                e.printStackTrace()
            }

        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }

        accessPreferences.clear()

    }

    companion object {

        private val TAG = "SecurityManager"
    }
}
