package com.example.caching.patterns.cacheaside;

import com.example.caching.config.RedisConfig;
import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.service.ProductPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Cache-Aside for a read-heavy product catalog.
 * Spring Cache annotations drive Redis lookups; on miss the method body loads from the DB
 * and the cache is populated automatically via {@link Cacheable}.
 */
@Service
public class CacheAsideProductService {

    private static final Logger log = LoggerFactory.getLogger(CacheAsideProductService.class);

    private final ProductPersistenceService persistenceService;

    public CacheAsideProductService(ProductPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Cacheable(value = RedisConfig.PRODUCT_CACHE, key = "#id")
    public Product getById(Long id) {
        log.debug("Cache-Aside MISS for product {} — loading from database", id);
        return persistenceService.findById(id);
    }

    @CachePut(value = RedisConfig.PRODUCT_CACHE, key = "#result.id")
    public Product create(ProductRequest request) {
        return persistenceService.create(request);
    }

    @CachePut(value = RedisConfig.PRODUCT_CACHE, key = "#id")
    public Product update(Long id, ProductRequest request) {
        return persistenceService.update(id, request);
    }

    @CacheEvict(value = RedisConfig.PRODUCT_CACHE, key = "#id")
    public void delete(Long id) {
        persistenceService.deleteById(id);
    }
}
