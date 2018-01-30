package com.enecuum.androidapp.models

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
                       val address: String)