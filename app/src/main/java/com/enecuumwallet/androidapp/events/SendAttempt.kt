package com.enecuumwallet.androidapp.events

import com.enecuumwallet.androidapp.models.Currency
import java.io.Serializable

/**
 * Created by oleg on 31.01.18.
 */
data class SendAttempt(
        val currency: Currency,
        val amount: Int?,
        val address: String?) : Serializable