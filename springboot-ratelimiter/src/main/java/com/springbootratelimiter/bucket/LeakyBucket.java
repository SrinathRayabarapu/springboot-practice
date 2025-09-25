package com.springbootratelimiter.bucket;

import java.time.Duration;
import java.time.Instant;

public class LeakyBucket {
    private final int capacity;
    private final double leakRatePerSecond;
    private double water;
    private Instant lastLeakTime;

    public LeakyBucket(int capacity, double leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRatePerSecond = leakRatePerSecond;
        this.water = 0;
        this.lastLeakTime = Instant.now();
    }

    public synchronized boolean tryConsume() {
        leak();
        if (water < capacity) {
            water++;
            return true;
        }
        return false;
    }

    private void leak() {
        Instant now = Instant.now();
        double leaked = Duration.between(lastLeakTime, now).toMillis() / 1000.0 * leakRatePerSecond;
        water = Math.max(0, water - leaked);
        lastLeakTime = now;
    }

    public synchronized double getWaterLevel() {
        leak();
        return water;
    }
}