package com.enecuum.androidapp.utils

/**
 * Created by oleg on 23.01.18.
 */
object Validator {
    fun validatePin(text : String) : Boolean {
        val regex = Regex("\\d{4}")
        return regex.matches(text)
    }
}