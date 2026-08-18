package com.example.devs.domain.dashboard.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.IntStream;

public enum TechTrendPeriod {
    WEEK("day", 7),
    MONTH("week", 4);

    private final String bucketUnit;
    private final int bucketCount;

    TechTrendPeriod(String bucketUnit, int bucketCount) {
        this.bucketUnit = bucketUnit;
        this.bucketCount = bucketCount;
    }

    public String bucketUnit() {
        return bucketUnit;
    }

    public LocalDate firstBucket(LocalDate today) {
        return switch (this) {
            case WEEK -> today.minusDays(bucketCount - 1L);
            case MONTH -> today
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .minusWeeks(bucketCount - 1L);
        };
    }

    public LocalDate endExclusive(LocalDate today) {
        return nextBucket(currentBucket(today));
    }

    public List<LocalDate> bucketDates(LocalDate today) {
        LocalDate firstBucket = firstBucket(today);

        return IntStream.range(0, bucketCount)
                .mapToObj(index -> addBuckets(firstBucket, index))
                .toList();
    }

    private LocalDate currentBucket(LocalDate today) {
        return switch (this) {
            case WEEK -> today;
            case MONTH -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        };
    }

    private LocalDate nextBucket(LocalDate date) {
        return addBuckets(date, 1);
    }

    private LocalDate addBuckets(LocalDate date, long amount) {
        return switch (this) {
            case WEEK -> date.plusDays(amount);
            case MONTH -> date.plusWeeks(amount);
        };
    }
}
