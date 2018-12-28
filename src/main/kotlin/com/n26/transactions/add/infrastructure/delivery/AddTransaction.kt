package com.n26.transactions.add.infrastructure.delivery

import com.n26.transactions.add.infrastructure.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime

data class AddTransaction(val amount: String, val timestamp: String) {
    companion object {
        @JvmStatic
        fun from(domain: AddTransactionDomain): AddTransaction {
            return AddTransaction(domain.amount.toString(), DateUtils.formatted(domain.timestamp))
        }
    }
}

data class AddTransactionDomain(internal val amount: BigDecimal, internal val timestamp: LocalDateTime)

