package com.n26.transactions

import arrow.core.Either
import com.n26.transactions.add.infrastructure.TransactionError
import com.n26.transactions.add.infrastructure.TransactionSuccess
import com.n26.transactions.add.domain.AddTransaction
import org.springframework.stereotype.Service

@Service
open class TransactionService {
    open fun addTransaction(request: AddTransaction): Either<TransactionError, TransactionSuccess> {
        return Either.right(TransactionSuccess())
    }
}
