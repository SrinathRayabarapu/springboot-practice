package com.example.caching.patterns.cacheaside;

import com.example.caching.config.CachingProperties;
import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.service.ProductPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheAsideProductServiceTest {

    @Mock
    private ProductPersistenceService persistenceService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    private CacheAsideProductService service;

    @BeforeEach
    void setUp() {
        CachingProperties properties = new CachingProperties();
        properties.getProduct().setTtlSeconds(60);
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        service = new CacheAsideProductService(persistenceService, redisTemplate, properties);
    }

    @Test
    void getByIdReturnsCachedValueWithoutHittingDatabase() {
        Product cached = product(1L);
        when(valueOperations.get("products:aside:1")).thenReturn(cached);

        Product result = service.getById(1L);

        assertThat(result).isSameAs(cached);
        verify(persistenceService, never()).findById(any());
    }

    @Test
    void getByIdLoadsFromDatabaseAndPopulatesCacheOnMiss() {
        Product product = product(2L);
        when(valueOperations.get("products:aside:2")).thenReturn(null);
        when(persistenceService.findById(2L)).thenReturn(product);

        Product result = service.getById(2L);

        assertThat(result).isEqualTo(product);
        verify(valueOperations).set(eq("products:aside:2"), eq(product), eq(Duration.ofSeconds(60)));
    }

    @Test
    void updateInvalidatesCache() {
        ProductRequest request = request();
        Product updated = product(3L);
        when(persistenceService.update(3L, request)).thenReturn(updated);

        service.update(3L, request);

        verify(redisTemplate).delete("products:aside:3");
    }

    private static Product product(Long id) {
        Product product = new Product("SKU-" + id, "Name", BigDecimal.TEN, 5);
        product.setId(id);
        return product;
    }

    private static ProductRequest request() {
        ProductRequest request = new ProductRequest();
        request.setSku("SKU-3");
        request.setName("Updated");
        request.setPrice(BigDecimal.ONE);
        request.setStock(1);
        return request;
    }
}
