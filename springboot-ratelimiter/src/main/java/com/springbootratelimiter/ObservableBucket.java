package com.springbootratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class ObservableBucket {
    private final Bucket bucket;
    private final Duration refillDuration;
    private Instant nextRefill;

    public ObservableBucket(long capacity, Duration refillDuration) {
        this.refillDuration = refillDuration;
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(capacity, refillDuration));
        this.bucket = Bucket.builder().addLimit(limit).build();
        this.nextRefill = Instant.now().plus(refillDuration);
    }

    public boolean tryConsume(long tokens) {
        boolean result = bucket.tryConsume(tokens);
        if (Instant.now().isAfter(nextRefill)) {
            System.out.println("Bucket refilled at " + LocalDateTime.now());
            nextRefill = Instant.now().plus(refillDuration);
        }
        return result;
    }

}