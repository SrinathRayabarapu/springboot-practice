package com.example.caching.patterns.writethrough;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteThroughProductServiceTest {

    @Mock
    private ProductPersistenceService persistenceService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    private WriteThroughProductService service;

    @BeforeEach
    void setUp() {
        CachingProperties properties = new CachingProperties();
        properties.getProduct().setTtlSeconds(120);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        service = new WriteThroughProductService(persistenceService, redisTemplate, properties);
    }

    @Test
    void updateWritesThroughToDatabaseAndCache() {
        ProductRequest request = new ProductRequest();
        request.setSku("SKU-9");
        request.setName("WriteThrough");
        request.setPrice(new BigDecimal("19.99"));
        request.setStock(3);

        Product updated = new Product(request.getSku(), request.getName(), request.getPrice(), request.getStock());
        updated.setId(9L);
        when(persistenceService.update(9L, request)).thenReturn(updated);

        Product result = service.update(9L, request);

        assertThat(result.getId()).isEqualTo(9L);
        verify(valueOperations).set(eq("products:writethrough:9"), eq(updated), eq(Duration.ofSeconds(120)));
    }
}
