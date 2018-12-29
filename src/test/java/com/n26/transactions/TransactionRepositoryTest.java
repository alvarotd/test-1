package com.n26.transactions;

import com.n26.transactions.statistics.domain.Statistics;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.n26.transactions.add.infrastructure.AddTransactionObjectMother.valid;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore("Pending implementation")
public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    @Ignore("Pending implementation")
    public void adds_all_the_transactions() {

        transactionRepository.addTransaction(valid());
        transactionRepository.addTransaction(valid());

        assertThat(transactionRepository.statisticsOfLast60Seconds().count()).isEqualTo(2);
    }

}
