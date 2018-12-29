package com.n26.transactions

import com.n26.transactions.add.domain.AddTransaction
import com.n26.transactions.statistics.domain.Statistics
import org.springframework.stereotype.Repository

@Repository
open class TransactionRepository {
    open fun addTransaction(request: AddTransaction) {

    }

    open fun statisticsOfLast60Seconds(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
