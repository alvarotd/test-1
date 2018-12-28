package com.n26.transactions.add.infrastructure;

import com.n26.transactions.add.domain.AddTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AddTransactionObjectMother {
    public static AddTransaction valid() {
        return new AddTransaction(new BigDecimal("12.3343"), LocalDateTime.now());
    }

    public static AddTransaction tooOld() {
        return new AddTransaction(new BigDecimal("12.3343"), LocalDateTime.MIN);
    }

    public static AddTransaction aged(LocalDateTime localDateTime) {
        return new AddTransaction(new BigDecimal("12.3343"), localDateTime);
    }

    public static AddTransaction inTheFuture() {
        return new AddTransaction(new BigDecimal("12.3343"), LocalDateTime.now().plusYears(10));
    }
}
