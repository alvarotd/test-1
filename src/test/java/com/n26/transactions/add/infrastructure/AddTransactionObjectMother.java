package com.n26.transactions.add.infrastructure;

import com.n26.transactions.domain.Transaction;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AddTransactionObjectMother {
    public static Transaction valid() {
        return new Transaction(new BigDecimal("12.3343"), now());
    }

    public static Transaction tooOld() {
        return new Transaction(new BigDecimal("12.3343"), now().minusYears(10));
    }

    public static Transaction aged(ZonedDateTime dateTime) {
        return new Transaction(new BigDecimal("12.3343"), dateTime);
    }

    public static Transaction inTheFuture() {
        return new Transaction(new BigDecimal("12.3343"), now().plusYears(10));
    }

    @NotNull
    private static ZonedDateTime now() {
        return ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
    }
}
