package com.n26.transactions.domain;

import arrow.core.Option;
import com.n26.transactions.statistics.domain.Statistics;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

import static java.math.BigDecimal.ZERO;

public class Snapshot {

    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal average = BigDecimal.ZERO;
    private int count = 0;
    private Option<BigDecimal> max = Option.Companion.empty();
    private Option<BigDecimal> min = Option.Companion.empty();

    public Snapshot() {
        initialize();
    }

    public boolean add(Transaction request) {
        if (request.expired()) {
            return false;
        }
        final BigDecimal requestAmount = request.getAmount();
        sum = sum.add(requestAmount);
        updateMax(requestAmount);
        updateMin(requestAmount);
        count++;
        average = updatedAverage();
        return true;
    }

    private void updateMax(BigDecimal requestAmount) {
        if (!max.isDefined()) {
            max = Option.Companion.just(requestAmount);
            return;
        }
        max = max.map(it -> {
            if (requestAmount.compareTo(it) > 0) {
                return requestAmount;
            } else {
                return it;
            }
        });
    }

    private void updateMin(BigDecimal requestAmount) {
        if (!min.isDefined()) {
            min = Option.Companion.just(requestAmount);
            return;
        }
        min = min.map(it -> {
            if (requestAmount.compareTo(it) < 0) {
                return requestAmount;
            } else {
                return it;
            }
        });
    }

    public void expire(Transaction request) {
        final BigDecimal amount = request.getAmount();
        sum = sum.subtract(amount);
        count--;
        average = updatedAverage();
    }

    private BigDecimal updatedAverage() {
        if (count == 0) {
            return ZERO;
        }
        final BigDecimal amount = sum;
        BigDecimal total = new BigDecimal(count);
        return amount.divide(total, MathContext.DECIMAL128);
    }

    public Statistics generateStatistics() {
        return Statistics.Companion.of(count,
                pair("sum", formatted(sum)),
                pair("avg", formatted(average)),
                pair("max", formatted(peekOrDefault(max, ZERO))),
                pair("min", formatted(peekOrDefault(min, ZERO))));
    }

    @Nullable
    private BigDecimal peekOrDefault(Option<BigDecimal> value, BigDecimal defaultValue) {
        if (value.isDefined()) {
            return value.orNull();
        } else {
            return defaultValue;
        }
    }

    private Pair<String, String> pair(String a, String b) {
        return new Pair<>(a, b);
    }

    private String formatted(BigDecimal t) {
        return t.setScale(2, RoundingMode.HALF_UP).toString();
    }


    public void clear() {
        reinitialize();
    }

    private void reinitialize() {
        initialize();
    }

    private void initialize() {
        sum = BigDecimal.ZERO;
        average = BigDecimal.ZERO;
        count = 0;
        max = Option.Companion.empty();
        min = Option.Companion.empty();
    }
}
