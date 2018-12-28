package com.n26.transactions

import arrow.core.Either
import arrow.core.left
import com.n26.transactions.add.infrastructure.TransactionError
import com.n26.transactions.add.infrastructure.TransactionSuccess
import com.n26.transactions.add.infrastructure.delivery.AddTransaction

open class TransactionService {
    open fun addTransaction(request: AddTransaction): Either<TransactionError, TransactionSuccess> {
        return Either.left(TransactionError())
    }
}
