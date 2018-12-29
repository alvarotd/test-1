package com.n26.transactions

import com.n26.transactions.add.domain.AddTransaction
import com.n26.transactions.statistics.domain.Statistics
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Repository
open class TransactionRepository {
    private val values: MutableList<AddTransaction> = mutableListOf()

    open fun addTransaction(request: AddTransaction) {
        this.values.add(request)
    }

    open fun statisticsOfLast60Seconds(): Statistics {
        val validValues = values.filter { !it.expired() }
        val count = validValues.size
        return Statistics.of(count,
                "sum" to sum(validValues),
                "avg" to avgOrDefault(validValues, BigDecimal.ZERO),
                "max" to maxOrDefault(validValues, BigDecimal.ZERO),
                "min" to minOrDefault(validValues, BigDecimal.ZERO))
    }

    private fun sum(values: List<AddTransaction>): String = values.fold(BigDecimal.ZERO) { acc, it -> acc.add(it.amount) }.formatted()

    private fun maxOrDefault(values: List<AddTransaction>, defaultValue: BigDecimal): String {
        return if (values.isEmpty()) {
            defaultValue
        } else {
            values.reduce { acc, ele ->
                if (acc.amount > ele.amount) {
                    acc
                } else {
                    ele
                }
            }.amount
        }.formatted()
    }

    private fun minOrDefault(values: List<AddTransaction>, defaultValue: BigDecimal): String {
        return if (values.isEmpty()) {
            defaultValue
        } else {
            values.reduce { acc, ele ->
                if (acc.amount > ele.amount) {
                    ele
                } else {
                    acc
                }
            }.amount
        }.formatted()
    }

    private fun avgOrDefault(values: List<AddTransaction>, defaultValue: BigDecimal): String {
        val quantity = BigDecimal(values.size)
        val result = if (quantity == BigDecimal.ZERO) {
            defaultValue
        } else {
            val sum = values.fold(BigDecimal.ZERO) { acc, it -> acc.add(it.amount) }
            sum.divide(quantity, MathContext.DECIMAL128)
        }
        return result.formatted()
    }

    private fun BigDecimal.formatted(): String {
        return this.setScale(2, RoundingMode.HALF_UP).toString()
    }

    open fun deleteAllTransactions() {
        this.values.clear()
    }
}

