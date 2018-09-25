package com.enecuumwallet.androidapp.events

import com.enecuumwallet.androidapp.models.Currency

/**
 * Created by oleg on 01.02.18.
 */
data class SendAddressChanged(val currency: Currency, val newValue: String)