package com.n26.transactions.statistics.infrastructure.delivery

import arrow.core.Either
import com.n26.transactions.TransactionService
import com.n26.transactions.add.domain.AddTransaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController("/statistics")
class TransactionController(private val service: TransactionService) {

    @GetMapping()
    fun getStatistics(): ResponseEntity<*> {
        TODO()
    }
}
