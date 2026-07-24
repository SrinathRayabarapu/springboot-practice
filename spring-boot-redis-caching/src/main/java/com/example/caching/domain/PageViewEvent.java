package com.example.caching.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "page_view_events")
public class PageViewEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String pagePath;

    @Column(nullable = false, length = 128)
    private String visitorId;

    @Column(length = 64)
    private String sessionId;

    @Column(nullable = false)
    private Instant viewedAt = Instant.now();

    public PageViewEvent() {
    }

    public PageViewEvent(String pagePath, String visitorId, String sessionId, Instant viewedAt) {
        this.pagePath = pagePath;
        this.visitorId = visitorId;
        this.sessionId = sessionId;
        this.viewedAt = viewedAt != null ? viewedAt : Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Instant getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Instant viewedAt) {
        this.viewedAt = viewedAt;
    }
}
