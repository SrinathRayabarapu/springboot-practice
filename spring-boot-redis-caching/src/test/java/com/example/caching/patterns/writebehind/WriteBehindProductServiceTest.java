package com.example.caching.patterns.writebehind;

import com.example.caching.config.CachingProperties;
import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.service.ProductPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteBehindProductServiceTest {

    @Mock
    private ProductPersistenceService persistenceService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    private WriteBehindProductService service;

    @BeforeEach
    void setUp() {
        CachingProperties properties = new CachingProperties();
        properties.getProduct().setTtlSeconds(60);
        properties.getWriteBehind().setBatchSize(10);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        service = new WriteBehindProductService(persistenceService, redisTemplate, properties);
    }

    @Test
    void createQueuesWriteAndFlushPersistsToDatabase() {
        ProductRequest request = new ProductRequest();
        request.setSku("SKU-WB");
        request.setName("WriteBehind");
        request.setPrice(new BigDecimal("5.00"));
        request.setStock(4);

        Product created = service.create(request);
        assertThat(created.getId()).isNegative();
        assertThat(service.pendingCount()).isEqualTo(1);

        Product saved = new Product(request.getSku(), request.getName(), request.getPrice(), request.getStock());
        saved.setId(100L);
        when(persistenceService.save(any(Product.class))).thenReturn(saved);

        service.flushPendingWrites();

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(persistenceService).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(service.pendingCount()).isZero();
    }
}
