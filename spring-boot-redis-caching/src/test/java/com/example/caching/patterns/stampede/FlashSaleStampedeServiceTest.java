package com.example.caching.patterns.stampede;

import com.example.caching.config.CachingProperties;
import com.example.caching.domain.Product;
import com.example.caching.service.ProductPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashSaleStampedeServiceTest {

    @Mock
    private ProductPersistenceService persistenceService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private RLock lock;

    private FlashSaleStampedeService service;

    @BeforeEach
    void setUp() {
        CachingProperties properties = new CachingProperties();
        properties.getProduct().setTtlSeconds(30);
        properties.getProduct().setStampedeLockWaitSeconds(1);
        properties.getProduct().setStampedeLockLeaseSeconds(5);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        service = new FlashSaleStampedeService(persistenceService, redisTemplate, redissonClient, properties);
    }

    @Test
    void getFlashSaleItemUsesLockAndLoadsDatabaseOnceOnMiss() throws Exception {
        Product product = new Product("SKU-1", "Flash Sale", BigDecimal.TEN, 1);
        product.setId(1L);

        when(valueOperations.get("products:flash-sale:1")).thenReturn(null, null);
        when(redissonClient.getLock("lock:flash-sale:product:1")).thenReturn(lock);
        when(lock.tryLock(1L, 5L, TimeUnit.SECONDS)).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        when(persistenceService.findById(1L)).thenReturn(product);

        Product result = service.getFlashSaleItem(1L);

        assertThat(result).isEqualTo(product);
        assertThat(service.getDbLoadCount()).isEqualTo(1);
        verify(persistenceService, times(1)).findById(1L);
        verify(lock).unlock();
    }

    @Test
    void getFlashSaleItemSkipsDatabaseWhenCacheIsWarm() {
        Product cached = new Product("SKU-2", "Cached Deal", BigDecimal.ONE, 2);
        cached.setId(2L);
        when(valueOperations.get("products:flash-sale:2")).thenReturn(cached);

        Product result = service.getFlashSaleItem(2L);

        assertThat(result).isSameAs(cached);
        verify(redissonClient, times(0)).getLock(anyString());
        verify(persistenceService, times(0)).findById(anyLong());
    }
}
