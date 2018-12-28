package com.n26.transactions.add.infrastructure;

import arrow.core.Either;
import com.n26.transactions.TransactionService;
import com.n26.transactions.add.infrastructure.delivery.AddTransaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionServiceTest {

    private TransactionService transactionService;

    @Before
    public void setUp() throws Exception {
        transactionService = new TransactionService();
    }

    @Test
    public void a_valid_transaction(){

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(new AddTransaction("1", "2"));

        assertThat(result.isRight()).isTrue();
    }
}
