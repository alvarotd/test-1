package com.n26.transactions.add.infrastructure

import arrow.core.Either
import com.n26.transactions.TransactionService
import com.n26.transactions.add.domain.AddTransaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController("/transactions")
class TransactionController(private val service: TransactionService) {

    @PostMapping()
    fun addTransaction(@Valid @RequestBody request: AddTransaction): ResponseEntity<*> {
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

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun cannotParseMessage(e: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build<Void>()
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun cannotParseAField(e: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build<Void>()
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
    object TransactionIsTooOld : TransactionError()
    object TransactionDateInTheFuture : TransactionError()
}
