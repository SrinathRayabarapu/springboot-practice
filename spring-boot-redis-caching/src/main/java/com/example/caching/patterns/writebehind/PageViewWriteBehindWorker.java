package com.example.caching.patterns.writebehind;

import com.example.caching.config.CachingProperties;
import com.example.caching.domain.PageViewEvent;
import com.example.caching.repository.PageViewEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Background worker that periodically drains the Redis page-view list and batch-inserts into the RDBMS.
 */
@Component
public class PageViewWriteBehindWorker {

    private static final Logger log = LoggerFactory.getLogger(PageViewWriteBehindWorker.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final PageViewEventRepository pageViewEventRepository;
    private final CachingProperties cachingProperties;

    public PageViewWriteBehindWorker(RedisTemplate<String, Object> redisTemplate,
                                     PageViewEventRepository pageViewEventRepository,
                                     CachingProperties cachingProperties) {
        this.redisTemplate = redisTemplate;
        this.pageViewEventRepository = pageViewEventRepository;
        this.cachingProperties = cachingProperties;
    }

    @Scheduled(fixedDelayString = "${caching.write-behind.flush-delay-ms:2000}")
    @Transactional
    public void drainQueue() {
        int batchSize = cachingProperties.getWriteBehind().getBatchSize();
        String queueKey = cachingProperties.getWriteBehind().getQueueKey();
        List<PageViewEvent> batch = new ArrayList<>(batchSize);

        for (int i = 0; i < batchSize; i++) {
            Object raw = redisTemplate.opsForList().rightPop(queueKey);
            if (raw == null) {
                break;
            }
            if (raw instanceof PageViewEvent) {
                batch.add((PageViewEvent) raw);
            } else {
                log.warn("Skipping unexpected write-behind payload type: {}", raw.getClass().getName());
            }
        }

        if (batch.isEmpty()) {
            return;
        }

        pageViewEventRepository.saveAll(batch);
        log.info("Write-Behind drained {} page-view event(s) into the database", batch.size());
    }
}
