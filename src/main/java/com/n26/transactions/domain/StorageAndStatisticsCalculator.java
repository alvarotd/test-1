package com.n26.transactions.domain;

import com.n26.transactions.add.infrastructure.DateUtils;
import com.n26.transactions.statistics.domain.Statistics;
import javafx.util.Duration;

import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StorageAndStatisticsCalculator {
    private final long delayInSecondsToExpire;
    private Snapshot snapshot = new Snapshot();
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public StorageAndStatisticsCalculator() {
        this(60);
    }

    public StorageAndStatisticsCalculator(long delayInSecondsToExpire) {
        this.delayInSecondsToExpire = delayInSecondsToExpire;
    }

    public synchronized void add(Transaction request) {
        final ZonedDateTime expiryDate = request.getTimestamp().plusSeconds(delayInSecondsToExpire);
        final long difference = DateUtils.now().until(expiryDate, ChronoUnit.MILLIS);
        if (snapshot.add(request)) {
            scheduledExecutorService.schedule(() -> snapshot.expire(request), difference, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized Statistics snapshot() {
        return snapshot.generateStatistics();
    }

    public synchronized void clear() {
        snapshot.clear();
    }
}
