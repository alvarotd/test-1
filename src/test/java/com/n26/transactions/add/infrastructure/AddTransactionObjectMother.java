package com.n26.transactions.add.infrastructure;

import com.n26.transactions.domain.Transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class AddTransactionObjectMother {
    public static Transaction valid() {
        return new Transaction(new BigDecimal("12.3343"), DateUtils.now());
    }

    public static Transaction tooOld() {
        return new Transaction(new BigDecimal("12.3343"), DateUtils.now().minusYears(10));
    }

    public static Transaction aged(ZonedDateTime dateTime) {
        return new Transaction(new BigDecimal("12.3343"), dateTime);
    }

    public static Transaction inTheFuture() {
        return new Transaction(new BigDecimal("12.3343"), DateUtils.now().plusYears(10));
    }

}
