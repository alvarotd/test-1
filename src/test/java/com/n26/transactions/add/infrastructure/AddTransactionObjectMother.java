package com.n26.transactions.add.infrastructure;

import com.n26.transactions.add.infrastructure.delivery.AddTransaction;

import java.math.BigDecimal;

import static com.n26.transactions.add.infrastructure.DateUtils.parseDate;

public class AddTransactionObjectMother {
    public static AddTransaction valid() {
        return new AddTransaction(new BigDecimal("12.3343"), parseDate("2018-07-17T09:59:51.312Z"));
    }
}
