package com.srinath.caching.patterns.cacheaside;

import com.srinath.caching.domain.Product;
import com.srinath.caching.domain.ProductRequest;
import com.srinath.caching.service.ProductPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheAsideProductServiceTest {

    @Mock
    private ProductPersistenceService persistenceService;

    private CacheAsideProductService service;

    @BeforeEach
    void setUp() {
        service = new CacheAsideProductService(persistenceService);
    }

    @Test
    void getByIdDelegatesToDatabaseOnCacheMissPath() {
        Product product = product(1L);
        when(persistenceService.findById(1L)).thenReturn(product);

        Product result = service.getById(1L);

        assertThat(result).isEqualTo(product);
        verify(persistenceService).findById(1L);
    }

    @Test
    void createPersistsAndReturnsProductForCachePut() {
        ProductRequest request = request();
        Product saved = product(2L);
        when(persistenceService.create(request)).thenReturn(saved);

        assertThat(service.create(request)).isEqualTo(saved);
        verify(persistenceService).create(request);
    }

    @Test
    void deleteEvictsViaPersistenceDelete() {
        service.delete(3L);
        verify(persistenceService).deleteById(3L);
    }

    private static Product product(Long id) {
        Product product = new Product("SKU-" + id, "Name", BigDecimal.TEN, 5);
        product.setId(id);
        return product;
    }

    private static ProductRequest request() {
        ProductRequest request = new ProductRequest();
        request.setSku("SKU-2");
        request.setName("Catalog Item");
        request.setPrice(BigDecimal.ONE);
        request.setStock(1);
        return request;
    }
}
