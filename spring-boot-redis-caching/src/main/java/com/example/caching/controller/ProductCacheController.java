package com.example.caching.controller;

import com.example.caching.domain.Product;
import com.example.caching.domain.ProductRequest;
import com.example.caching.patterns.cacheaside.CacheAsideProductService;
import com.example.caching.patterns.stampede.CacheStampedeProtectionService;
import com.example.caching.patterns.writebehind.WriteBehindProductService;
import com.example.caching.patterns.writethrough.WriteThroughProductService;
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

@Validated
@RestController
@RequestMapping("/api/products")
public class ProductCacheController {

    private final CacheAsideProductService cacheAsideProductService;
    private final WriteThroughProductService writeThroughProductService;
    private final WriteBehindProductService writeBehindProductService;
    private final CacheStampedeProtectionService cacheStampedeProtectionService;

    public ProductCacheController(CacheAsideProductService cacheAsideProductService,
                                  WriteThroughProductService writeThroughProductService,
                                  WriteBehindProductService writeBehindProductService,
                                  CacheStampedeProtectionService cacheStampedeProtectionService) {
        this.cacheAsideProductService = cacheAsideProductService;
        this.writeThroughProductService = writeThroughProductService;
        this.writeBehindProductService = writeBehindProductService;
        this.cacheStampedeProtectionService = cacheStampedeProtectionService;
    }

    @GetMapping("/cache-aside/{id}")
    public Product getCacheAside(@PathVariable Long id) {
        return cacheAsideProductService.getById(id);
    }

    @PostMapping("/cache-aside")
    public ResponseEntity<Product> createCacheAside(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cacheAsideProductService.create(request));
    }

    @PutMapping("/cache-aside/{id}")
    public Product updateCacheAside(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return cacheAsideProductService.update(id, request);
    }

    @DeleteMapping("/cache-aside/{id}")
    public ResponseEntity<Void> deleteCacheAside(@PathVariable Long id) {
        cacheAsideProductService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/write-through/{id}")
    public Product getWriteThrough(@PathVariable Long id) {
        return writeThroughProductService.getById(id);
    }

    @PostMapping("/write-through")
    public ResponseEntity<Product> createWriteThrough(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(writeThroughProductService.create(request));
    }

    @PutMapping("/write-through/{id}")
    public Product updateWriteThrough(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return writeThroughProductService.update(id, request);
    }

    @DeleteMapping("/write-through/{id}")
    public ResponseEntity<Void> deleteWriteThrough(@PathVariable Long id) {
        writeThroughProductService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/write-behind/{id}")
    public Product getWriteBehind(@PathVariable Long id) {
        return writeBehindProductService.getById(id);
    }

    @PostMapping("/write-behind")
    public ResponseEntity<Product> createWriteBehind(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(writeBehindProductService.create(request));
    }

    @PutMapping("/write-behind/{id}")
    public Product updateWriteBehind(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return writeBehindProductService.update(id, request);
    }

    @DeleteMapping("/write-behind/{id}")
    public ResponseEntity<Void> deleteWriteBehind(@PathVariable Long id) {
        writeBehindProductService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/write-behind/pending-count")
    public Map<String, Integer> writeBehindPendingCount() {
        return Map.of("pending", writeBehindProductService.pendingCount());
    }

    @PostMapping("/write-behind/flush")
    public Map<String, String> flushWriteBehind() {
        writeBehindProductService.flushPendingWrites();
        return Map.of("status", "flushed");
    }

    @GetMapping("/stampede/{id}")
    public Product getWithStampedeProtection(@PathVariable Long id) {
        return cacheStampedeProtectionService.getById(id);
    }

    @DeleteMapping("/stampede/{id}/cache")
    public ResponseEntity<Void> evictStampedeCache(@PathVariable Long id) {
        cacheStampedeProtectionService.evict(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stampede/db-load-count")
    public Map<String, Integer> stampedeDbLoadCount() {
        return Map.of("dbLoads", cacheStampedeProtectionService.getDbLoadCount());
    }

    @PostMapping("/stampede/db-load-count/reset")
    public Map<String, Integer> resetStampedeDbLoadCount() {
        cacheStampedeProtectionService.resetDbLoadCount();
        return Map.of("dbLoads", 0);
    }
}
