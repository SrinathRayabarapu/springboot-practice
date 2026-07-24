package com.example.caching.patterns.writethrough;

import com.example.caching.config.CachingProperties;
import com.example.caching.config.RedisCacheConfig;
import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.service.ProductPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * Write-Through: every write goes to cache and database in the same request path.
 * Reads prefer the cache; on miss the DB value is loaded and written through to cache.
 */
@Service
public class WriteThroughProductService {

    private static final Logger log = LoggerFactory.getLogger(WriteThroughProductService.class);

    private final ProductPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CachingProperties cachingProperties;

    public WriteThroughProductService(ProductPersistenceService persistenceService,
                                      RedisTemplate<String, Object> redisTemplate,
                                      CachingProperties cachingProperties) {
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
        this.cachingProperties = cachingProperties;
    }

    public Product getById(Long id) {
        String key = cacheKey(id);
        Product cached = (Product) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.debug("Write-Through HIT for product {}", id);
            return cached;
        }

        Product product = persistenceService.findById(id);
        redisTemplate.opsForValue().set(key, product, ttl());
        return product;
    }

    @Transactional
    public Product create(ProductRequest request) {
        Product saved = persistenceService.create(request);
        redisTemplate.opsForValue().set(cacheKey(saved.getId()), saved, ttl());
        log.debug("Write-Through created product {} in DB and cache", saved.getId());
        return saved;
    }

    @Transactional
    public Product update(Long id, ProductRequest request) {
        Product updated = persistenceService.update(id, request);
        redisTemplate.opsForValue().set(cacheKey(id), updated, ttl());
        log.debug("Write-Through updated product {} in DB and cache", id);
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        persistenceService.deleteById(id);
        redisTemplate.delete(cacheKey(id));
    }

    private Duration ttl() {
        return Duration.ofSeconds(cachingProperties.getProduct().getTtlSeconds());
    }

    static String cacheKey(Long id) {
        return RedisCacheConfig.PRODUCT_CACHE + ":writethrough:" + id;
    }
}
