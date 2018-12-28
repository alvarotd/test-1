package com.n26.transactions.add.infrastructure

import arrow.core.Either
import com.n26.transactions.TransactionService
import com.n26.transactions.add.infrastructure.delivery.AddTransaction
import org.springframework.http.ResponseEntity

class TransactionController(private val service: TransactionService) {
    fun addTransaction(request: AddTransaction): ResponseEntity<*> {
        val map = service.addTransaction(request).map {
            ResponseEntity.ok().build<String>()
        }.mapLeft {
            ResponseEntity.ok().build<String>()
        }
        return x(map)
    }

    private fun <T> x(map: Either<T, T>): T {
        return when (map) {
            is Either.Left -> map.a
            is Either.Right -> map.b
        }
    }
}

class TransactionSuccess {

}

class TransactionError {

}
