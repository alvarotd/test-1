package com.n26.transactions.add.infrastructure

import arrow.core.Either
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.n26.transactions.add.domain.AddTransactionX
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TransactionsObjectMapper(val objectMapper: ObjectMapper) {
    fun tryParsing(requestRaw: String): Either<ResponseEntity<*>, AddTransactionX> {
        val request: AddTransactionX
        return try {
            request = objectMapper.readValue(requestRaw)
            Either.right(request)
        } catch (e: Exception) {
            Either.left(ResponseEntity.status(HttpStatus.BAD_REQUEST).build<Void>())
        }
    }

}
