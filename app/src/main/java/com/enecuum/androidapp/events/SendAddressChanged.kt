package com.enecuum.androidapp.events

import com.enecuum.androidapp.models.Currency

/**
 * Created by oleg on 01.02.18.
 */
data class SendAddressChanged(val currency: Currency, val newValue: String)