package com.n26.transactions.add.infrastructure;

import arrow.core.Either;
import com.n26.transactions.add.infrastructure.delivery.TransactionError;
import com.n26.transactions.add.infrastructure.delivery.TransactionSuccess;
import com.n26.transactions.domain.TransactionRepository;
import com.n26.transactions.domain.TransactionService;
import com.n26.transactions.domain.Transaction;
import com.n26.transactions.add.infrastructure.delivery.TransactionError.TransactionDateInTheFuture;
import com.n26.transactions.add.infrastructure.delivery.TransactionError.TransactionIsTooOld;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;

import static com.n26.transactions.add.infrastructure.AddTransactionObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() throws Exception {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    public void a_valid_transaction() {
        final Transaction request = valid();

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(request);

        assertThat(result.isRight()).isTrue();
        verify(transactionRepository).addTransaction(request);
    }

    @Test
    public void a_valid_transaction_almost_expired() {

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(aged(DateUtils.now().minusSeconds(59)));

        assertThat(result.isRight()).isTrue();
    }

    @Test
    public void a_transaction_too_old() {

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(tooOld());

        assertThat(result).isEqualTo(Either.Companion.left(TransactionIsTooOld.INSTANCE));
    }

    @Test
    public void a_transaction_in_the_future() {

        final Either<TransactionError, TransactionSuccess> result = transactionService.addTransaction(inTheFuture());

        assertThat(result).isEqualTo(Either.Companion.left(TransactionDateInTheFuture.INSTANCE));
    }

    @Test
    public void delegate_the_delete_transactions_call() {

        transactionService.deleteAllTransactions();

        verify(transactionRepository).deleteAllTransactions();
    }
}
