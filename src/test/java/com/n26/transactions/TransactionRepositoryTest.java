package com.n26.transactions;

import arrow.core.Option;
import com.n26.transactions.domain.Transaction;
import com.n26.transactions.domain.TransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static com.n26.transactions.add.infrastructure.AddTransactionObjectMother.tooOld;
import static com.n26.transactions.add.infrastructure.AddTransactionObjectMother.valid;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    public void adds_all_the_valid_transactions() {

        transactionRepository.addTransaction(tooOld());
        transactionRepository.addTransaction(valid());
        transactionRepository.addTransaction(valid());

        assertThat(transactionRepository.statisticsOfLast60Seconds().count()).isEqualTo(2);
    }


    @Test
    public void sums_the_transactions() {

        transactionRepository.addTransaction(valid());
        transactionRepository.addTransaction(valid());

        assertThat(get("sum")).isEqualTo(value("24.67"));
    }

    @Test
    public void averages_the_transactions() {

        transactionRepository.addTransaction(valid());
        transactionRepository.addTransaction(valid());

        assertThat(get("avg")).isEqualTo(value("12.33"));
    }

    @Test
    public void averages_the_transactions_using_correct_division() {

        transactionRepository.addTransaction(new Transaction(new BigDecimal("2"), ZonedDateTime.now()));
        transactionRepository.addTransaction(new Transaction(new BigDecimal("0"), ZonedDateTime.now()));
        transactionRepository.addTransaction(new Transaction(new BigDecimal("0"), ZonedDateTime.now()));

        assertThat(get("avg")).isEqualTo(value("0.67"));
    }

    @Test
    public void default_average_value_is_zero() {

        //do not add any transactions

        assertThat(get("avg")).isEqualTo(value("0.00"));
    }

    @Test
    public void default_max_value_is_zero() {

        //do not add any transactions

        assertThat(get("max")).isEqualTo(value("0.00"));
    }

    @Test
    public void default_min_value_is_zero() {

        //do not add any transactions

        assertThat(get("min")).isEqualTo(value("0.00"));
    }

    @Test
    public void max_the_transactions() {

        transactionRepository.addTransaction(valid());
        transactionRepository.addTransaction(new Transaction(new BigDecimal("13"), ZonedDateTime.now()));

        assertThat(get("max")).isEqualTo(value("13.00"));
    }

    @Test
    public void min_the_transactions() {

        transactionRepository.addTransaction(valid());
        transactionRepository.addTransaction(new Transaction(new BigDecimal("13"), ZonedDateTime.now()));

        assertThat(get("min")).isEqualTo(value("12.33"));
    }

    @NotNull
    private Option<String> get(String sum) {
        return transactionRepository.statisticsOfLast60Seconds().get(sum);
    }

    @NotNull
    private Option<String> value(String s) {
        return Option.Companion.just(s);
    }

    //Pending:
    // O(1) for adding
    // O(1) for fetching
    // limit for last 60 seconds
    // correct behaviour for all
    // check rounding

}
