package com.n26.transactions.statistics.domain

import arrow.core.Option

data class Statistics private constructor(val count: Pair<String, Int>, private val pairs: List<Pair<String, String>>) {

    companion object {
        fun of(count: Int, vararg pairs: Pair<String, String>): Statistics {
            return of(count, pairs.asList())
        }

        fun of(count: Int, pairs: List<Pair<String, String>>): Statistics {
            return Statistics("count" to count, pairs)
        }
    }

    fun count(): Int {
        return count.second
    }

    fun get(key: String): Option<String> {
        val possiblePair = pairs.find { it -> it.first == key }
        return Option.fromNullable(possiblePair)
                .map { it.second }
    }
}
