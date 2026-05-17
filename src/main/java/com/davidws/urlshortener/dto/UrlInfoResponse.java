package com.davidws.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlInfoResponse {

    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private long accessCount;

    public UrlInfoResponse(String originalUrl, String shortCode,
                           LocalDateTime createdAt, LocalDateTime expiresAt,
                           long accessCount) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.accessCount = accessCount;
    }

    public String getOriginalUrl() { return originalUrl; }
    public String getShortCode() { return shortCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public long getAccessCount() { return accessCount; }
}