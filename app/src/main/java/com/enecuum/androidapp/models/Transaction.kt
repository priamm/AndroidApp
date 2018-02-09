package com.enecuum.androidapp.models

import java.io.Serializable

/**
 * Created by oleg on 30.01.18.
 */
enum class TransactionType {
    Send,
    Receive
}
data class Transaction(val transactionType: TransactionType,
                       val timestamp: Long,
                       val amount: Double,
                       val address: String,
                       val mode: SendReceiveMode) : Serializable