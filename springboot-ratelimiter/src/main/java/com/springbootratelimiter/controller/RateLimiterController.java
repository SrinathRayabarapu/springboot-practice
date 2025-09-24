package com.springbootratelimiter.controller;

import com.springbootratelimiter.ObservableBucket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
public class RateLimiterController {

    private final ObservableBucket observableBucket = new ObservableBucket(5, Duration.ofSeconds(10));

    @GetMapping("/limited")
    public ResponseEntity<String> limitedEndpoint() {
        if (observableBucket.tryConsume(1)) {
            return ResponseEntity.ok("Request successful");
        }
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
    }

    // Add more endpoints to demonstrate other rate limiting strategies
}