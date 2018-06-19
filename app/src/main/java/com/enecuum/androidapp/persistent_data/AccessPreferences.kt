package com.enecuum.androidapp.persistent_data


import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import java.util.*

class AccessPreferences {

    private val CLASSES = ArrayList<Class<*>>()
    private var prefs: SharedPreferences? = null// cache

    init {
        CLASSES.add(String::class.java)
        CLASSES.add(Boolean::class.java)
        CLASSES.add(Int::class.java)
        CLASSES.add(Long::class.java)
        CLASSES.add(Float::class.java)
        CLASSES.add(Set::class.java)
    }

    constructor(prefs: SharedPreferences) {
        this.prefs = prefs
    }

    private constructor() {}

    private fun getPrefs(): SharedPreferences {
        return prefs ?: throw IllegalArgumentException("Shared preference is not set")
    }


    fun <T> put(key: String,
                value: T) {
        val ed = _put(key, value)
        ed.apply()
    }

    fun <T> commit(key: String,
                   value: T): Boolean {
        return _put(key, value).commit()
    }

    private fun <T> _put(key: String?,
                         value: T?): Editor {
        if (key == null)
            throw NullPointerException("Null keys are not permitted")
        val ed = getPrefs().edit()
        if (value == null) {
            ed.putString(key, null)
        } else if (value is String)
            ed.putString(key, value as String?)
        else if (value is Boolean)
            ed.putBoolean(key, (value as Boolean?)!!)
        else if (value is Int)
            ed.putInt(key, (value as Int?)!!)
        else if (value is Long)
            ed.putLong(key, (value as Long?)!!)
        else if (value is Float)
            ed.putFloat(key, (value as Float?)!!)
        else if (value is Set<*>) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                throw IllegalArgumentException(
                        "You can add sets in the preferences only after API " + Build.VERSION_CODES.HONEYCOMB)
            }
            val dummyVariable = ed.putStringSet(key, value as Set<String>?)// this set can contain whatever it wants - don't be fooled by the
            // Set<String> cast
        } else
            throw IllegalArgumentException("The given value : " + value
                    + " cannot be persisted")// while int "is-a" long (will be converted to long) Integer IS NOT a

        return ed
    }

    operator fun <T> get(key: String?,
                         defaultValue: T?): T? {
        if (key == null)
            throw NullPointerException("Null keys are not permitted")

        if (defaultValue == null) {

            if (!getPrefs().contains(key)) return null
            // if the key does exist I get the value and..
            val value = getPrefs().all[key] ?: return null

            val valueClass = value.javaClass
            for (cls in CLASSES) {
                if (valueClass.isAssignableFrom(cls)) {
                    return valueClass.cast(value) as T

                }
            }
            throw IllegalStateException("Unknown class for value :\n\t"
                    + value + "\nstored in preferences")
        } else if (defaultValue is String)
            return getPrefs()
                    .getString(key, defaultValue as String?) as T
        else if (defaultValue is Boolean)
            return getPrefs(
            ).getBoolean(key, (defaultValue as Boolean?)!!) as T
        else if (defaultValue is Int)
            return getPrefs(
            ).getInt(key, (defaultValue as Int?)!!) as T
        else if (defaultValue is Long)
            return getPrefs()
                    .getLong(key, (defaultValue as Long?)!!) as T
        else if (defaultValue is Float)
            return getPrefs()
                    .getFloat(key, (defaultValue as Float?)!!) as T
        else if (defaultValue is Set<*>) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                throw IllegalArgumentException(
                        "You can add sets in the preferences only after API " + Build.VERSION_CODES.HONEYCOMB)
            }
            return getPrefs().getStringSet(key,
                    defaultValue as Set<String>?) as T
        } else
            throw IllegalArgumentException(defaultValue.toString() + " cannot be persisted in SharedPreferences")// the order should not matter
    }

    operator fun contains(key: String?): Boolean {
        if (key == null)
            throw NullPointerException("Null keys are not permitted")
        return getPrefs().contains(key)
    }

    fun clear(): Boolean {
        return getPrefs().edit().clear().commit()
    }

    fun remove(key: String?): Boolean {
        if (key == null)
            throw NullPointerException("Null keys are not permitted")
        return getPrefs().edit().remove(key).commit()
    }

    fun registerListener(lis: OnSharedPreferenceChangeListener?) {
        if (lis == null) throw NullPointerException("Null listener")
        getPrefs().registerOnSharedPreferenceChangeListener(lis)
    }

    fun unregisterListener(lis: OnSharedPreferenceChangeListener?) {
        if (lis == null) throw NullPointerException("Null listener")
        getPrefs().unregisterOnSharedPreferenceChangeListener(lis)
    }

    fun callListener(lis: OnSharedPreferenceChangeListener?, key: String?) {
        if (lis == null) throw NullPointerException("Null listener")
        if (key == null)
            throw NullPointerException("Null keys are not permitted")
        lis.onSharedPreferenceChanged(getPrefs(), key)
    }

    private fun checkSetContainsStrings(set: Set<*>): Set<String> {
        if (!set.isEmpty()) {
            for (`object` in set) {
                if (`object` !is String)
                    throw IllegalArgumentException(
                            "The given set does not contain strings only")
            }
        }
        return set as Set<String>
    }
}