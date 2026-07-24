package com.example.caching.patterns.writethrough;

import com.example.caching.config.CachingProperties;
import com.example.caching.domain.Product;
import com.example.caching.domain.StockUpdateRequest;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteThroughInventoryServiceTest {

    @Mock
    private ProductPersistenceService persistenceService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    private WriteThroughInventoryService service;

    @BeforeEach
    void setUp() {
        CachingProperties properties = new CachingProperties();
        properties.getProduct().setTtlSeconds(120);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        service = new WriteThroughInventoryService(persistenceService, redisTemplate, properties);
    }

    @Test
    void updateStockWritesDatabaseThenRedis() {
        Product existing = product(9L, 3);
        Product saved = product(9L, 10);
        StockUpdateRequest request = new StockUpdateRequest();
        request.setStock(10);

        when(persistenceService.findById(9L)).thenReturn(existing);
        when(persistenceService.save(existing)).thenReturn(saved);

        Product result = service.updateStock(9L, request);

        assertThat(result.getStock()).isEqualTo(10);
        verify(valueOperations).set(eq("products:inventory:9"), eq(saved), eq(Duration.ofSeconds(120)));
    }

    @Test
    void updateStockPropagatesRedisFailureForTransactionRollback() {
        Product existing = product(9L, 3);
        Product saved = product(9L, 10);
        StockUpdateRequest request = new StockUpdateRequest();
        request.setStock(10);

        when(persistenceService.findById(9L)).thenReturn(existing);
        when(persistenceService.save(existing)).thenReturn(saved);
        doThrow(new RuntimeException("redis down"))
                .when(valueOperations).set(eq("products:inventory:9"), eq(saved), eq(Duration.ofSeconds(120)));

        assertThatThrownBy(() -> service.updateStock(9L, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Write-through Redis update failed");
    }

    private static Product product(Long id, int stock) {
        Product product = new Product("SKU-" + id, "Inventory", BigDecimal.TEN, stock);
        product.setId(id);
        return product;
    }
}
