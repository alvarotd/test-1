package com.n26.transactions.add.infrastructure;

import com.n26.transactions.add.domain.AddTransaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class AddTransactionObjectMother {
    public static AddTransaction valid() {
        return new AddTransaction(new BigDecimal("12.3343"), ZonedDateTime.now());
    }

    public static AddTransaction tooOld() {
        return new AddTransaction(new BigDecimal("12.3343"), ZonedDateTime.now().minusYears(10));
    }

    public static AddTransaction aged(ZonedDateTime dateTime) {
        return new AddTransaction(new BigDecimal("12.3343"), dateTime);
    }

    public static AddTransaction inTheFuture() {
        return new AddTransaction(new BigDecimal("12.3343"), ZonedDateTime.now().plusYears(10));
    }
}
