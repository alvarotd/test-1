package com.n26.transactions.statistics.infrastructure.delivery

import com.n26.transactions.domain.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/statistics")
class StatisticsController(private val service: TransactionService) {

    @GetMapping()
    fun getStatistics(): ResponseEntity<*> {
        return ResponseEntity.ok(service.getStatistics())
    }
}
