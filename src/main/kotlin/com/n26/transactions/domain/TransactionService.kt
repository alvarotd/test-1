package com.n26.transactions.domain

import arrow.core.Either
import com.n26.transactions.add.infrastructure.delivery.TransactionError
import com.n26.transactions.add.infrastructure.delivery.TransactionSuccess
import com.n26.transactions.statistics.domain.Statistics
import org.springframework.stereotype.Service

@Service
open class TransactionService(private val transactionRepository: TransactionRepository) {
    open fun addTransaction(request: Transaction): Either<TransactionError, TransactionSuccess> {
        if(request.expired()){
            return Either.left(TransactionError.TransactionIsTooOld)
        } else if(request.isInTheFuture()){
            return Either.left(TransactionError.TransactionDateInTheFuture)
        }
        transactionRepository.addTransaction(request)
        return Either.right(TransactionSuccess())
    }

    open fun getStatistics(): Statistics {
        return transactionRepository.statisticsOfLast60Seconds()
    }

    open fun deleteAllTransactions() {
        transactionRepository.deleteAllTransactions()
    }
}
