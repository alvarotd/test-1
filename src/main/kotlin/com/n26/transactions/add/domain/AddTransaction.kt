package com.n26.transactions.add.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class AddTransaction(internal val amount: BigDecimal, internal val timestamp: LocalDateTime)

