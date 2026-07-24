package com.example.caching.patterns.cacheaside;

import com.example.caching.config.CachingProperties;
import com.example.caching.config.RedisCacheConfig;
import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.service.ProductPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Cache-Aside (lazy loading): application reads cache first; on miss, loads from DB and populates cache.
 * Writes update the DB, then invalidate (or refresh) the cache entry.
 */
@Service
public class CacheAsideProductService {

    private static final Logger log = LoggerFactory.getLogger(CacheAsideProductService.class);

    private final ProductPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CachingProperties cachingProperties;

    public CacheAsideProductService(ProductPersistenceService persistenceService,
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
            log.debug("Cache-Aside HIT for product {}", id);
            return cached;
        }

        log.debug("Cache-Aside MISS for product {}", id);
        Product product = persistenceService.findById(id);
        redisTemplate.opsForValue().set(key, product, ttl());
        return product;
    }

    public Product create(ProductRequest request) {
        Product saved = persistenceService.create(request);
        redisTemplate.opsForValue().set(cacheKey(saved.getId()), saved, ttl());
        return saved;
    }

    public Product update(Long id, ProductRequest request) {
        Product updated = persistenceService.update(id, request);
        redisTemplate.delete(cacheKey(id));
        log.debug("Cache-Aside invalidated product {}", id);
        return updated;
    }

    public void delete(Long id) {
        persistenceService.deleteById(id);
        redisTemplate.delete(cacheKey(id));
    }

    private Duration ttl() {
        return Duration.ofSeconds(cachingProperties.getProduct().getTtlSeconds());
    }

    static String cacheKey(Long id) {
        return RedisCacheConfig.PRODUCT_CACHE + ":aside:" + id;
    }
}
