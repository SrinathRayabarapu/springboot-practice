package com.srinath.caching.patterns.writethrough;

import com.srinath.caching.config.CachingProperties;
import com.srinath.caching.domain.Product;
import com.srinath.caching.domain.ProductRequest;
import com.srinath.caching.domain.StockUpdateRequest;
import com.srinath.caching.service.ProductPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * Write-Through for inventory stock levels requiring tight consistency.
 * RDBMS and Redis are updated synchronously inside the same {@link Transactional} boundary;
 * a Redis failure aborts the DB transaction.
 */
@Slf4j
@Service
public class WriteThroughInventoryService {

    static final String INVENTORY_KEY_PREFIX = "products:inventory:";

    private final ProductPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CachingProperties cachingProperties;

    public WriteThroughInventoryService(ProductPersistenceService persistenceService,
                                        RedisTemplate<String, Object> redisTemplate,
                                        CachingProperties cachingProperties) {
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
        this.cachingProperties = cachingProperties;
    }

    public Product getStock(Long id) {
        String key = cacheKey(id);
        Product cached = (Product) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.debug("Write-Through inventory HIT for product {}", id);
            return cached;
        }

        Product product = persistenceService.findById(id);
        writeThroughToRedis(key, product);
        return product;
    }

    @Transactional
    public Product create(ProductRequest request) {
        Product saved = persistenceService.create(request);
        writeThroughToRedis(cacheKey(saved.getId()), saved);
        log.debug("Write-Through created inventory for product {}", saved.getId());
        return saved;
    }

    /**
     * Primary write-through use case: stock mutation hits DB then Redis in one transaction.
     */
    @Transactional
    public Product updateStock(Long id, StockUpdateRequest request) {
        Product product = persistenceService.findById(id);
        product.setStock(request.getStock());
        Product saved = persistenceService.save(product);
        writeThroughToRedis(cacheKey(id), saved);
        log.debug("Write-Through updated stock for product {} to {}", id, saved.getStock());
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        persistenceService.deleteById(id);
        redisTemplate.delete(cacheKey(id));
    }

    private void writeThroughToRedis(String key, Product product) {
        try {
            redisTemplate.opsForValue().set(key, product, ttl());
        } catch (RuntimeException ex) {
            // Propagate so the surrounding @Transactional method rolls back the RDBMS write.
            throw new IllegalStateException("Write-through Redis update failed for key " + key, ex);
        }
    }

    private Duration ttl() {
        return Duration.ofSeconds(cachingProperties.getProduct().getTtlSeconds());
    }

    static String cacheKey(Long id) {
        return INVENTORY_KEY_PREFIX + id;
    }
}
