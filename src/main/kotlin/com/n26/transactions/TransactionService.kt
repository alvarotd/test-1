package com.n26.transactions

import arrow.core.Either
import com.n26.transactions.add.infrastructure.TransactionError
import com.n26.transactions.add.infrastructure.TransactionSuccess
import com.n26.transactions.add.domain.AddTransaction
import com.n26.transactions.statistics.domain.Statistics
import org.springframework.stereotype.Service

@Service
open class TransactionService(private val transactionRepository: TransactionRepository) {
    open fun addTransaction(request: AddTransaction): Either<TransactionError, TransactionSuccess> {
        if(request.expired()){
            return Either.left(TransactionError.TransactionIsTooOld)
        } else if(request.isInTheFuture()){
            return Either.left(TransactionError.TransactionDateInTheFuture)
        }
        transactionRepository.addTransaction(request)
        return Either.right(TransactionSuccess())
    }

    open fun getStatistics(): Statistics {
        TODO()
    }

    open fun deleteAllTransactions() {

    }
}
