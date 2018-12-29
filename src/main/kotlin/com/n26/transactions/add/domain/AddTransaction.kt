package com.n26.transactions.add.domain

import arrow.core.Either
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.toOption
import com.n26.transactions.add.infrastructure.DateUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

data class AddTransaction(val amount: BigDecimal, val timestamp: ZonedDateTime) {
    fun expired(): Boolean {
        return ZonedDateTime.now().isAfter(timestamp.plusMinutes(1))
    }

    fun isInTheFuture(): Boolean {
        return ZonedDateTime.now().isBefore(timestamp)
    }
}

data class AddTransactionX(val amount: String, val timestamp: String) {
    fun toDomain(): Either<ResponseEntity<*>, AddTransaction> {

        val timestamp = try {
            Either.right(DateUtils.parseDate(this.timestamp))
        } catch (e: Exception) {
            Either.left(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build<Void>())
        }

        val amount = try {
            Either.right(BigDecimal(this.amount))
        } catch (e: Exception) {
            Either.left(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build<Void>())
        }

        return timestamp.flatMap {timestampV->
            amount.map { amountV->
                AddTransaction(amountV, timestampV)
            }
        }
    }
}

