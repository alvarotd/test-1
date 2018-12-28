package com.n26.transactions.add.infrastructure

import arrow.core.Either
import com.n26.transactions.TransactionService
import com.n26.transactions.add.infrastructure.delivery.AddTransaction

class TransactionController(service: TransactionService) {
    fun addTransaction(request: AddTransaction): Either<TransactionError, TransactionSuccess> {
        return Either.right(TransactionSuccess())
    }
}

class TransactionSuccess {

}

class TransactionError {

}
