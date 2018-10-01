package com.enecuumwallet.androidapp.utils

import com.enecuumwallet.androidapp.persistent_data.Constants

/**
 * Created by oleg on 23.01.18.
 */
object Validator {
    fun validatePin(text : String) : Boolean {
        val regex = Regex("\\d{4}")
        return regex.matches(text)
    }

    fun validateSeed(text : String) : Boolean {
        val wordCount = wordCount(text)
        return wordCount == Constants.SEED_PHRASE_SIZE
    }

    fun seedRemainCount(text : String) : Int = Constants.SEED_PHRASE_SIZE - wordCount(text)

    private fun wordCount(text: String): Int = text.split(" ").filter { it.isNotEmpty() }.size
}