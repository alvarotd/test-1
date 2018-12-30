package com.n26.transactions.domain

import com.n26.transactions.add.infrastructure.DateUtils
import java.math.BigDecimal
import java.time.ZonedDateTime

data class Transaction(val amount: BigDecimal, val timestamp: ZonedDateTime) {
    fun expired(): Boolean {
        return DateUtils.now().isAfter(timestamp.plusMinutes(1))
    }

    fun isInTheFuture(): Boolean {
        return DateUtils.now().isBefore(timestamp)
    }
}

