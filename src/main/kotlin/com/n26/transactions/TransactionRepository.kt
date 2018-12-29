package com.n26.transactions

import com.n26.transactions.add.domain.AddTransaction
import com.n26.transactions.statistics.domain.Statistics
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.math.RoundingMode

@Repository
open class TransactionRepository {
    private val values: MutableList<AddTransaction> = mutableListOf()

    open fun addTransaction(request: AddTransaction) {
        this.values.add(request)
    }

    open fun statisticsOfLast60Seconds(): Statistics {
        val count = values.size
        return Statistics.of(count,
                "sum" to sum(values),
                "avg" to avg(values),
                "max" to max(values),
                "min" to min(values))
    }

    private fun sum(values: List<AddTransaction>): String = values.fold(BigDecimal.ZERO) { acc, it -> acc.add(it.amount) }.formatted()

    private fun max(values: List<AddTransaction>): String {
        return values.reduce { acc, ele ->
            if (acc.amount > ele.amount) {
                acc
            } else {
                ele
            }
        }.amount.formatted()
    }

    private fun min(values: List<AddTransaction>): String {
        return values.reduce { acc, ele ->
            if (acc.amount > ele.amount) {
                ele
            } else {
                acc
            }
        }.amount.formatted()
    }

    private fun avg(values: List<AddTransaction>): String {
        val sum = values.fold(BigDecimal.ZERO) { acc, it -> acc.add(it.amount) }
        val quantity = BigDecimal(values.size)
        return sum.divide(quantity).formatted()
    }

    private fun BigDecimal.formatted(): String {
        return this.setScale(2, RoundingMode.HALF_UP).toString()  //TODO AGB set proper MathContext
    }
}

