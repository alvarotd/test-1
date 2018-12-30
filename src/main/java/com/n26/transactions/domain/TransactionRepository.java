package com.n26.transactions.domain;

import com.n26.transactions.statistics.domain.Statistics;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {
    private final StorageAndStatisticsCalculator values;

    public TransactionRepository(){
        this(new StorageAndStatisticsCalculator());
    }

    public TransactionRepository(StorageAndStatisticsCalculator storageAndStatisticsCalculator) {
        values = storageAndStatisticsCalculator;
    }

    public void addTransaction(Transaction request) {
        synchronized (values) {
            this.values.add(request);
        }
    }

    public Statistics statisticsOfLast60Seconds() {
        synchronized (values) {
            return values.snapshot();
        }
    }

    public void deleteAllTransactions() {
        synchronized (values) {
            this.values.clear();
        }
    }
}

