package com.srinath.caching.patterns.writebehind;

import com.srinath.caching.config.CachingProperties;
import com.srinath.caching.domain.PageViewEvent;
import com.srinath.caching.domain.PageViewRequest;
import com.srinath.caching.repository.PageViewEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageViewWriteBehindServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ListOperations<String, Object> listOperations;
    @Mock
    private PageViewEventRepository pageViewEventRepository;

    private CachingProperties properties;
    private PageViewWriteBehindService service;
    private PageViewWriteBehindWorker worker;

    @BeforeEach
    void setUp() {
        properties = new CachingProperties();
        properties.getWriteBehind().setQueueKey("analytics:page-views");
        properties.getWriteBehind().setBatchSize(2);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        service = new PageViewWriteBehindService(redisTemplate, properties);
        worker = new PageViewWriteBehindWorker(redisTemplate, pageViewEventRepository, properties);
    }

    @Test
    void recordPushesEventOntoRedisList() {
        PageViewRequest request = new PageViewRequest();
        request.setPagePath("/products/1");
        request.setVisitorId("v-1");
        request.setSessionId("s-1");

        PageViewEvent event = service.record(request);

        assertThat(event.getPagePath()).isEqualTo("/products/1");
        verify(listOperations).leftPush(eq("analytics:page-views"), any(PageViewEvent.class));
    }

    @Test
    void workerDrainsRedisListAndBatchInserts() {
        PageViewEvent first = new PageViewEvent("/a", "v1", "s1", null);
        PageViewEvent second = new PageViewEvent("/b", "v2", "s2", null);
        when(listOperations.rightPop("analytics:page-views")).thenReturn(first, second);

        worker.drainQueue();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<PageViewEvent>> captor = ArgumentCaptor.forClass(List.class);
        verify(pageViewEventRepository).saveAll(captor.capture());
        assertThat(captor.getValue()).containsExactly(first, second);
    }

    @Test
    void workerSkipsPersistWhenQueueEmpty() {
        when(listOperations.rightPop("analytics:page-views")).thenReturn(null);

        worker.drainQueue();

        verify(pageViewEventRepository, never()).saveAll(any());
    }
}
