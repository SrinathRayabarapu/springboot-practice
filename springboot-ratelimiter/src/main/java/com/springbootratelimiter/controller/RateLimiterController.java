package com.springbootratelimiter.controller;

import com.springbootratelimiter.bucket.LeakyBucket;
import com.springbootratelimiter.bucket.ObservableBucket;
import com.springbootratelimiter.bucket.SlidingWindow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
public class RateLimiterController {

    private final ObservableBucket observableBucket = new ObservableBucket(5, Duration.ofSeconds(20));
    private final LeakyBucket leakyBucket = new LeakyBucket(5, 0.5); // 5 capacity, 0.5 req/sec
    private final SlidingWindow slidingWindow = new SlidingWindow(5, 10000); // 5 requests per 10 seconds

    @GetMapping("/simpleRateLimiter")
    public ResponseEntity<String> limitedEndpoint() {
        if (observableBucket.tryConsume(1)) {
            return ResponseEntity.ok("Request successful");
        }
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
    }

    // Add more endpoints to demonstrate other rate limiting strategies

    // add a get method to return available tokens
    @GetMapping("/availableTokens")
    public ResponseEntity<String> getAvailableTokens() {
        long availableTokens = observableBucket.getAvailableTokens();
        return ResponseEntity.ok("Available tokens: " + availableTokens);
    }

    @GetMapping("/leakyBucketRateLimiter")
    public ResponseEntity<String> leakyBucketEndpoint() {
        if (leakyBucket.tryConsume()) {
            return ResponseEntity.ok("Leaky bucket request successful");
        }
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests (leaky bucket)");
    }

    @GetMapping("/leakyBucketLevel")
    public ResponseEntity<String> getLeakyBucketLevel() {
        double level = leakyBucket.getWaterLevel();
        return ResponseEntity.ok("Leaky bucket water level: " + level);
    }

    @GetMapping("/slidingWindowRateLimiter")
    public ResponseEntity<String> slidingWindowEndpoint() {
        if (slidingWindow.tryConsume()) {
            return ResponseEntity.ok("Sliding Window bucket request successful");
        }
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests (Sliding Window bucket)");
    }

}