package com.n26.transactions.statistics.domain;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StatisticsObjectMother {

    @NotNull
    public static Statistics allZeroes(){
        final List<Pair<String, String>> stats = Arrays.asList(
                new Pair<>("sum", "0.00"),
                new Pair<>("avg", "0.00"),
                new Pair<>("max", "0.00"),
                new Pair<>("min", "0.00"));
        return Statistics.Companion.of(0, stats);
    }

    @NotNull
    public static Statistics random(){
        final Random random = new Random();
        final List<Pair<String, String>> stats = Arrays.asList(
                new Pair<>("sum", looksLikeABigDecimalFormatted(random)),
                new Pair<>("avg", looksLikeABigDecimalFormatted(random)),
                new Pair<>("max", looksLikeABigDecimalFormatted(random)),
                new Pair<>("min", looksLikeABigDecimalFormatted(random)));
        return Statistics.Companion.of(random.nextInt(), stats);
    }

    private static String looksLikeABigDecimalFormatted(Random random) {
        return String.format("%s.%s%s", random.nextInt(10), random.nextInt(10), random.nextInt(10));
    }
}
