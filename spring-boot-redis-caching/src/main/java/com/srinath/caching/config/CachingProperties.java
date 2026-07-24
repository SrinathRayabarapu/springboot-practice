package com.srinath.caching.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "caching")
public class CachingProperties {

    private final Product product = new Product();
    private final WriteBehind writeBehind = new WriteBehind();

    public Product getProduct() {
        return product;
    }

    public WriteBehind getWriteBehind() {
        return writeBehind;
    }

    public static class Product {
        private long ttlSeconds = 300;
        private long stampedeLockWaitSeconds = 3;
        private long stampedeLockLeaseSeconds = 10;

        public long getTtlSeconds() {
            return ttlSeconds;
        }

        public void setTtlSeconds(long ttlSeconds) {
            this.ttlSeconds = ttlSeconds;
        }

        public long getStampedeLockWaitSeconds() {
            return stampedeLockWaitSeconds;
        }

        public void setStampedeLockWaitSeconds(long stampedeLockWaitSeconds) {
            this.stampedeLockWaitSeconds = stampedeLockWaitSeconds;
        }

        public long getStampedeLockLeaseSeconds() {
            return stampedeLockLeaseSeconds;
        }

        public void setStampedeLockLeaseSeconds(long stampedeLockLeaseSeconds) {
            this.stampedeLockLeaseSeconds = stampedeLockLeaseSeconds;
        }
    }

    public static class WriteBehind {
        private String queueKey = "analytics:page-views";
        private long flushDelayMs = 2000;
        private int batchSize = 50;

        public String getQueueKey() {
            return queueKey;
        }

        public void setQueueKey(String queueKey) {
            this.queueKey = queueKey;
        }

        public long getFlushDelayMs() {
            return flushDelayMs;
        }

        public void setFlushDelayMs(long flushDelayMs) {
            this.flushDelayMs = flushDelayMs;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
    }
}
