package com.n26.transactions.domain

import java.math.BigDecimal
import java.time.ZonedDateTime

data class Transaction(val amount: BigDecimal, val timestamp: ZonedDateTime) {
    fun expired(): Boolean {
        return ZonedDateTime.now().isAfter(timestamp.plusMinutes(1))
    }

    fun isInTheFuture(): Boolean {
        return ZonedDateTime.now().isBefore(timestamp)
    }
}

