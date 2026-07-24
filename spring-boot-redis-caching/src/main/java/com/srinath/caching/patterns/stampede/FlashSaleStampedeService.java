package com.srinath.caching.patterns.stampede;

import com.srinath.caching.config.CachingProperties;
import com.srinath.caching.domain.Product;
import com.srinath.caching.service.ProductPersistenceService;
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
 * Cache stampede / thundering-herd prevention for high-concurrency flash-sale item access.
 * On miss, a Redisson {@link RLock} ensures only one thread loads from the DB and populates Redis;
 * concurrent callers wait on the lock and then serve from the warm cache.
 */
@Service
public class FlashSaleStampedeService {

    private static final Logger log = LoggerFactory.getLogger(FlashSaleStampedeService.class);
    static final String FLASH_SALE_KEY_PREFIX = "products:flash-sale:";

    private final ProductPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final CachingProperties cachingProperties;
    private final AtomicInteger dbLoadCount = new AtomicInteger();

    public FlashSaleStampedeService(ProductPersistenceService persistenceService,
                                    RedisTemplate<String, Object> redisTemplate,
                                    RedissonClient redissonClient,
                                    CachingProperties cachingProperties) {
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.cachingProperties = cachingProperties;
    }

    public Product getFlashSaleItem(Long id) {
        String key = cacheKey(id);
        Product cached = (Product) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        RLock lock = redissonClient.getLock("lock:flash-sale:product:" + id);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(
                    cachingProperties.getProduct().getStampedeLockWaitSeconds(),
                    cachingProperties.getProduct().getStampedeLockLeaseSeconds(),
                    TimeUnit.SECONDS
            );

            // Double-check: winner may have populated the cache while we waited for the lock.
            cached = (Product) redisTemplate.opsForValue().get(key);
            if (cached != null) {
                log.debug("Flash-sale stampede: cache populated while waiting for product {}", id);
                return cached;
            }

            if (!acquired) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Flash-sale item temporarily unavailable (lock contention) for product " + id);
            }

            log.debug("Flash-sale stampede: loading product {} from database", id);
            Product product = persistenceService.findById(id);
            dbLoadCount.incrementAndGet();
            redisTemplate.opsForValue().set(key, product, ttl());
            return product;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Interrupted while waiting for flash-sale cache lock", ex);
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
        return FLASH_SALE_KEY_PREFIX + id;
    }
}
