package com.enecuum.androidapp.events

import com.enecuum.androidapp.models.Currency
import java.io.Serializable

/**
 * Created by oleg on 31.01.18.
 */
data class SendAttempt(
        val currency: Currency,
        val amount: Int?,
        val address: String?) : Serializable