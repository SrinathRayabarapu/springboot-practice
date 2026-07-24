package com.example.caching.domain;

import javax.validation.constraints.NotBlank;

public class PageViewRequest {

    @NotBlank
    private String pagePath;

    @NotBlank
    private String visitorId;

    private String sessionId;

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
}
