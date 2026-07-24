package com.srinath.caching.patterns.writebehind;

import com.srinath.caching.config.CachingProperties;
import com.srinath.caching.domain.PageViewEvent;
import com.srinath.caching.domain.PageViewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Write-Behind intake for high-frequency page-view / analytics events.
 * Events are pushed to a Redis List immediately; a scheduled worker drains and batch-inserts to the DB.
 */
@Service
public class PageViewWriteBehindService {

    private static final Logger log = LoggerFactory.getLogger(PageViewWriteBehindService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final CachingProperties cachingProperties;

    public PageViewWriteBehindService(RedisTemplate<String, Object> redisTemplate,
                                      CachingProperties cachingProperties) {
        this.redisTemplate = redisTemplate;
        this.cachingProperties = cachingProperties;
    }

    public PageViewEvent record(PageViewRequest request) {
        PageViewEvent event = new PageViewEvent(
                request.getPagePath(),
                request.getVisitorId(),
                request.getSessionId(),
                Instant.now()
        );
        redisTemplate.opsForList().leftPush(queueKey(), event);
        log.debug("Write-Behind enqueued page view for path={}", event.getPagePath());
        return event;
    }

    public long pendingCount() {
        Long size = redisTemplate.opsForList().size(queueKey());
        return size == null ? 0L : size;
    }

    String queueKey() {
        return cachingProperties.getWriteBehind().getQueueKey();
    }
}
