package com.springbootratelimiter.bucket;

import java.util.Deque;
import java.util.LinkedList;

public class SlidingWindow {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final Deque<Long> timestamps = new LinkedList<>();

    public SlidingWindow(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }

    public synchronized boolean tryConsume() {
        long now = System.currentTimeMillis();
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > windowSizeMillis) {
            timestamps.pollFirst();
        }
        if (timestamps.size() < maxRequests) {
            timestamps.addLast(now);
            return true;
        }
        return false;
    }

    public synchronized int getCurrentCount() {
        long now = System.currentTimeMillis();
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > windowSizeMillis) {
            timestamps.pollFirst();
        }
        return timestamps.size();
    }
}
