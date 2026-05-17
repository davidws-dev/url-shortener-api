package com.davidws.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ShortenRequest {

    @NotBlank(message = "A URL original é obrigatória")
    @Size(max = 2048, message = "URL demasiado longa (máx. 2048 caracteres)")
    @Pattern(
            regexp = "^https?://.*",
            message = "A URL deve começar com http:// ou https://"
    )
    private String originalUrl;

    private LocalDateTime expiresAt;

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}