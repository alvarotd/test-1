package com.n26.transactions.statistics.domain

data class Statistics private constructor(val count: Pair<String, Int>, val pairs: List<Pair<String, String>>) {
    companion object {
        fun of(count: Int, vararg pairs: Pair<String, String>): Statistics {
            return of(count, pairs.asList())
        }

        fun of(count: Int, pairs: List<Pair<String, String>>): Statistics {
            return Statistics("count" to count, pairs)
        }
    }
}
