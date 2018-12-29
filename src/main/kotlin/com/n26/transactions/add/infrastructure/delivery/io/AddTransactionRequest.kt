package com.n26.transactions.add.infrastructure.delivery.io

import arrow.core.Either
import arrow.core.flatMap
import com.n26.transactions.domain.Transaction
import com.n26.transactions.add.infrastructure.DateUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal
import java.time.ZonedDateTime

data class AddTransactionRequest(val amount: String, val timestamp: String) {
    fun toDomain(): Either<ResponseEntity<*>, Transaction> {

        val (timestamp, amount) = validate()

        return timestamp.flatMap { timestampV ->
            amount.map { amountV ->
                Transaction(amountV, timestampV)
            }
        }
    }

    private fun validate(): Pair<Either<ResponseEntity<Void>, ZonedDateTime>, Either<ResponseEntity<Void>, BigDecimal>> {
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
        return Pair(timestamp, amount)
    }
}