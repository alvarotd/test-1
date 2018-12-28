package com.n26.transactions.add.infrastructure

import arrow.core.Either
import com.n26.transactions.TransactionService
import com.n26.transactions.add.infrastructure.delivery.AddTransaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/transactions")
class TransactionController(private val service: TransactionService) {

    @PostMapping()
    fun addTransaction(@RequestBody request: AddTransaction): ResponseEntity<*> {
        val map = service.addTransaction(request)
                .map {
                    ResponseEntity.status(HttpStatus.CREATED).build<String>()
                }.mapLeft {
                    mapper(it)
                }
        return extractResult(map)
    }

    private fun mapper(error: TransactionError): ResponseEntity<*> {
        return TransactionStatusCodes.map(error)
    }

    private fun <T> extractResult(map: Either<T, T>): T {
        return when (map) {
            is Either.Left -> map.a
            is Either.Right -> map.b
        }
    }
}

object TransactionStatusCodes {
    fun map(error: TransactionError): ResponseEntity<*> {
        return when (error) {
            is TransactionError.TransactionDateInTheFuture -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build<Void>()
            is TransactionError.TransactionIsTooOld -> ResponseEntity.status(HttpStatus.NO_CONTENT).build<Void>()
        }
    }
}

class TransactionSuccess {

}

sealed class TransactionError {
    class TransactionIsTooOld() : TransactionError()
    class TransactionDateInTheFuture() : TransactionError()
}
