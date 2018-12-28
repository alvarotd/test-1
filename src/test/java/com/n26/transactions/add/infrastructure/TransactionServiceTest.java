package com.n26.transactions.add.infrastructure;

import arrow.core.Either;
import com.n26.transactions.TransactionService;
import com.n26.transactions.add.infrastructure.TransactionError.TransactionDateInTheFuture;
import com.n26.transactions.add.infrastructure.TransactionError.TransactionIsTooOld;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static com.n26.transactions.add.infrastructure.AddTransactionObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionServiceTest {

    private TransactionService transactionService;

    @Before
    public void setUp() throws Exception {
        transactionService = new TransactionService();
    }

    @Test
    public void a_valid_transaction(){

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(valid());

        assertThat(result.isRight()).isTrue();
    }

    @Test
    public void a_valid_transaction_almost_expired(){

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(aged(LocalDateTime.now().minusSeconds(59)));

        assertThat(result.isRight()).isTrue();
    }

    @Test
    public void a_transaction_too_old(){

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(tooOld());

        assertThat(result).isEqualTo(Either.Companion.left(TransactionIsTooOld.INSTANCE));
    }
    @Test
    public void a_transaction_in_the_future(){

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(inTheFuture());

        assertThat(result).isEqualTo(Either.Companion.left(TransactionDateInTheFuture.INSTANCE));
    }
}
