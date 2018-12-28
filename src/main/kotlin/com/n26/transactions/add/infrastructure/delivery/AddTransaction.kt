package com.n26.transactions.add.infrastructure.delivery

import java.math.BigDecimal
import java.time.LocalDateTime

data class AddTransaction(private val amount: BigDecimal, private val timestamp: LocalDateTime)
