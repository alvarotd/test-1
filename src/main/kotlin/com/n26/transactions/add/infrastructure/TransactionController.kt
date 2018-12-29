package com.n26.transactions.add.infrastructure

import arrow.core.Either
import com.n26.transactions.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("/transactions")
class TransactionController(private val service: TransactionService, private val objectMapper: TransactionsObjectMapper) {

    @PostMapping()
    fun addTransaction(@RequestBody requestRaw: String): ResponseEntity<*> {
        val tryParsing = objectMapper.tryParsing(requestRaw)
        if (tryParsing.isLeft()) {
            return (tryParsing as Either.Left).a
        }
        val toDomain = (tryParsing as Either.Right).b.toDomain()
        val result = toDomain
                .map { request ->
                    try {
                        val map = service.addTransaction(request)
                                .map {
                                    ResponseEntity.status(HttpStatus.CREATED).build<String>()
                                }.mapLeft {
                                    mapper(it)
                                }
                        extractResult(map)
                    } catch (e: IllegalArgumentException) {
                        ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build<Void>()
                    }
                }
        return extractResult(result)
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

//    @ExceptionHandler(HttpMessageNotReadableException::class)
//    fun cannotParseMessage(e: Exception): ResponseEntity<*> {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build<Void>()
//    }
//
//    @ExceptionHandler(IllegalArgumentException::class)
//    fun cannotParseAField(e: Exception): ResponseEntity<*> {
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build<Void>()
//    }

    @DeleteMapping()
    fun deleteAllTransactions(): ResponseEntity<*> {
        service.deleteAllTransactions()
        return ResponseEntity.noContent().build<Void>()
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
