package com.example.caching.patterns.writebehind;

import com.example.caching.config.CachingProperties;
import com.example.caching.config.RedisCacheConfig;
import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.service.ProductPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Write-Behind (write-back): updates hit the cache immediately and are queued for asynchronous DB persistence.
 */
@Service
public class WriteBehindProductService {

    private static final Logger log = LoggerFactory.getLogger(WriteBehindProductService.class);

    private final ProductPersistenceService persistenceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CachingProperties cachingProperties;
    private final ConcurrentLinkedQueue<Long> pendingIds = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Long, Product> pendingWrites = new ConcurrentHashMap<>();
    private final AtomicLong temporaryIdSequence = new AtomicLong(-1);

    public WriteBehindProductService(ProductPersistenceService persistenceService,
                                     RedisTemplate<String, Object> redisTemplate,
                                     CachingProperties cachingProperties) {
        this.persistenceService = persistenceService;
        this.redisTemplate = redisTemplate;
        this.cachingProperties = cachingProperties;
    }

    public Product getById(Long id) {
        Product pending = pendingWrites.get(id);
        if (pending != null) {
            return pending;
        }

        String key = cacheKey(id);
        Product cached = (Product) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        Product product = persistenceService.findById(id);
        redisTemplate.opsForValue().set(key, product, ttl());
        return product;
    }

    public Product create(ProductRequest request) {
        long tempId = temporaryIdSequence.getAndDecrement();
        Product product = new Product(request.getSku(), request.getName(), request.getPrice(), request.getStock());
        product.setId(tempId);
        enqueue(product);
        redisTemplate.opsForValue().set(cacheKey(tempId), product, ttl());
        log.debug("Write-Behind queued create with temporary id {}", tempId);
        return product;
    }

    public Product update(Long id, ProductRequest request) {
        Product current = getById(id);
        current.setName(request.getName());
        current.setPrice(request.getPrice());
        current.setStock(request.getStock());
        current.touch();
        enqueue(current);
        redisTemplate.opsForValue().set(cacheKey(id), current, ttl());
        log.debug("Write-Behind queued update for product {}", id);
        return current;
    }

    public void delete(Long id) {
        pendingWrites.remove(id);
        pendingIds.remove(id);
        redisTemplate.delete(cacheKey(id));
        if (id > 0) {
            persistenceService.deleteById(id);
        }
    }

    public int pendingCount() {
        return pendingWrites.size();
    }

    @Scheduled(fixedDelayString = "${caching.write-behind.flush-delay-ms:2000}")
    public void flushPendingWrites() {
        int batchSize = cachingProperties.getWriteBehind().getBatchSize();
        List<Product> batch = new ArrayList<>(batchSize);

        while (batch.size() < batchSize) {
            Long id = pendingIds.poll();
            if (id == null) {
                break;
            }
            Product product = pendingWrites.remove(id);
            if (product != null) {
                batch.add(product);
            }
        }

        if (batch.isEmpty()) {
            return;
        }

        for (Product product : batch) {
            flushOne(product);
        }
        log.info("Write-Behind flushed {} pending product write(s)", batch.size());
    }

    private void flushOne(Product product) {
        try {
            Long originalId = product.getId();
            if (originalId != null && originalId < 0) {
                product.setId(null);
                Product saved = persistenceService.save(product);
                redisTemplate.delete(cacheKey(originalId));
                redisTemplate.opsForValue().set(cacheKey(saved.getId()), saved, ttl());
            } else {
                Product saved = persistenceService.save(product);
                redisTemplate.opsForValue().set(cacheKey(saved.getId()), saved, ttl());
            }
        } catch (Exception ex) {
            log.error("Write-Behind flush failed for product id={}", product.getId(), ex);
            enqueue(product);
        }
    }

    private void enqueue(Product product) {
        pendingWrites.put(product.getId(), product);
        if (!pendingIds.contains(product.getId())) {
            pendingIds.offer(product.getId());
        }
    }

    private Duration ttl() {
        return Duration.ofSeconds(cachingProperties.getProduct().getTtlSeconds());
    }

    static String cacheKey(Long id) {
        return RedisCacheConfig.PRODUCT_CACHE + ":writebehind:" + id;
    }

    Map<Long, Product> pendingSnapshot() {
        return Map.copyOf(pendingWrites);
    }
}
