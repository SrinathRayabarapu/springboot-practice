package com.example.caching.patterns.stampede;

import com.example.caching.config.CachingProperties;
import com.example.caching.config.RedisCacheConfig;
import com.example.caching.domain.Product;
import com.example.caching.service.ProductPersistenceService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cache stampede / thundering-herd protection: on miss, only one caller loads from DB under a Redisson lock;
 * others wait briefly then re-read the cache.
 */
@Service
public class CacheStampedeProtectionService {

    private static final Logger log = LoggerFactory.getLogger(CacheStampedeProtectionService.class);

    private final ProductPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final CachingProperties cachingProperties;
    private final AtomicInteger dbLoadCount = new AtomicInteger();

    public CacheStampedeProtectionService(ProductPersistenceService persistenceService,
                                          RedisTemplate<String, Object> redisTemplate,
                                          RedissonClient redissonClient,
                                          CachingProperties cachingProperties) {
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.cachingProperties = cachingProperties;
    }

    public Product getById(Long id) {
        String key = cacheKey(id);
        Product cached = (Product) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        String lockKey = "lock:product:stampede:" + id;
        RLock lock = redissonClient.getLock(lockKey);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(
                    cachingProperties.getProduct().getStampedeLockWaitSeconds(),
                    cachingProperties.getProduct().getStampedeLockLeaseSeconds(),
                    TimeUnit.SECONDS
            );

            cached = (Product) redisTemplate.opsForValue().get(key);
            if (cached != null) {
                log.debug("Stampede protection: cache populated while waiting for product {}", id);
                return cached;
            }

            if (!acquired) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Unable to acquire stampede lock for product " + id);
            }

            log.debug("Stampede protection: loading product {} from database", id);
            Product product = persistenceService.findById(id);
            dbLoadCount.incrementAndGet();
            redisTemplate.opsForValue().set(key, product, ttl());
            return product;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Interrupted while waiting for cache lock", ex);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void evict(Long id) {
        redisTemplate.delete(cacheKey(id));
    }

    public int getDbLoadCount() {
        return dbLoadCount.get();
    }

    public void resetDbLoadCount() {
        dbLoadCount.set(0);
    }

    private Duration ttl() {
        return Duration.ofSeconds(cachingProperties.getProduct().getTtlSeconds());
    }

    static String cacheKey(Long id) {
        return RedisCacheConfig.PRODUCT_CACHE + ":stampede:" + id;
    }
}
