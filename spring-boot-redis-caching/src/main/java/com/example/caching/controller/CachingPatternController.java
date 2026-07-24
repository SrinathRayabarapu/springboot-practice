package com.example.caching.controller;

import com.example.caching.domain.PageViewEvent;
import com.example.caching.domain.PageViewRequest;
import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.domain.StockUpdateRequest;
import com.example.caching.patterns.cacheaside.CacheAsideProductService;
import com.example.caching.patterns.stampede.FlashSaleStampedeService;
import com.example.caching.patterns.writebehind.PageViewWriteBehindService;
import com.example.caching.patterns.writebehind.PageViewWriteBehindWorker;
import com.example.caching.patterns.writethrough.WriteThroughInventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * HTTP surface for exercising each distributed caching pattern under {@code /api/v1/cache}.
 */
@Validated
@RestController
@RequestMapping("/api/v1/cache")
public class CachingPatternController {

    private final CacheAsideProductService cacheAsideProductService;
    private final WriteThroughInventoryService writeThroughInventoryService;
    private final PageViewWriteBehindService pageViewWriteBehindService;
    private final PageViewWriteBehindWorker pageViewWriteBehindWorker;
    private final FlashSaleStampedeService flashSaleStampedeService;

    public CachingPatternController(CacheAsideProductService cacheAsideProductService,
                                    WriteThroughInventoryService writeThroughInventoryService,
                                    PageViewWriteBehindService pageViewWriteBehindService,
                                    PageViewWriteBehindWorker pageViewWriteBehindWorker,
                                    FlashSaleStampedeService flashSaleStampedeService) {
        this.cacheAsideProductService = cacheAsideProductService;
        this.writeThroughInventoryService = writeThroughInventoryService;
        this.pageViewWriteBehindService = pageViewWriteBehindService;
        this.pageViewWriteBehindWorker = pageViewWriteBehindWorker;
        this.flashSaleStampedeService = flashSaleStampedeService;
    }

    // --- Cache-Aside: product catalog ---

    @GetMapping("/aside/products/{id}")
    public Product getCacheAside(@PathVariable Long id) {
        return cacheAsideProductService.getById(id);
    }

    @PostMapping("/aside/products")
    public ResponseEntity<Product> createCacheAside(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cacheAsideProductService.create(request));
    }

    @PutMapping("/aside/products/{id}")
    public Product updateCacheAside(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return cacheAsideProductService.update(id, request);
    }

    @DeleteMapping("/aside/products/{id}")
    public ResponseEntity<Void> deleteCacheAside(@PathVariable Long id) {
        cacheAsideProductService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Write-Through: inventory stock ---

    @GetMapping("/write-through/inventory/{id}")
    public Product getInventory(@PathVariable Long id) {
        return writeThroughInventoryService.getStock(id);
    }

    @PostMapping("/write-through/inventory")
    public ResponseEntity<Product> createInventory(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(writeThroughInventoryService.create(request));
    }

    @PutMapping("/write-through/inventory/{id}/stock")
    public Product updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
        return writeThroughInventoryService.updateStock(id, request);
    }

    @DeleteMapping("/write-through/inventory/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        writeThroughInventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Write-Behind: page-view analytics ---

    @PostMapping("/write-behind/page-views")
    public ResponseEntity<PageViewEvent> recordPageView(@Valid @RequestBody PageViewRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageViewWriteBehindService.record(request));
    }

    @GetMapping("/write-behind/page-views/pending-count")
    public Map<String, Long> pageViewPendingCount() {
        return Map.of("pending", pageViewWriteBehindService.pendingCount());
    }

    @PostMapping("/write-behind/page-views/flush")
    public Map<String, String> flushPageViews() {
        pageViewWriteBehindWorker.drainQueue();
        return Map.of("status", "flushed");
    }

    // --- Stampede: flash-sale item access ---

    @GetMapping("/stampede/flash-sale/{id}")
    public Product getFlashSaleItem(@PathVariable Long id) {
        return flashSaleStampedeService.getFlashSaleItem(id);
    }

    @DeleteMapping("/stampede/flash-sale/{id}/cache")
    public ResponseEntity<Void> evictFlashSaleCache(@PathVariable Long id) {
        flashSaleStampedeService.evict(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stampede/flash-sale/db-load-count")
    public Map<String, Integer> flashSaleDbLoadCount() {
        return Map.of("dbLoads", flashSaleStampedeService.getDbLoadCount());
    }

    @PostMapping("/stampede/flash-sale/db-load-count/reset")
    public Map<String, Integer> resetFlashSaleDbLoadCount() {
        flashSaleStampedeService.resetDbLoadCount();
        return Map.of("dbLoads", 0);
    }
}
