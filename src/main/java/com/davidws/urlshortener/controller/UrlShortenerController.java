package com.davidws.urlshortener.controller;

import com.davidws.urlshortener.dto.ShortenRequest;
import com.davidws.urlshortener.dto.ShortenResponse;
import com.davidws.urlshortener.dto.UrlInfoResponse;
import com.davidws.urlshortener.entity.UrlEntity;
import com.davidws.urlshortener.exception.UrlNotFoundException;
import com.davidws.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@Valid @RequestBody ShortenRequest request) {
        UrlEntity entity = urlShortenerService.shortenUrl(
                request.getOriginalUrl(), request.getExpiresAt()
        );
        String shortUrl = "http://localhost:8080/" + entity.getShortCode();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ShortenResponse(entity.getShortCode(), shortUrl));
    }

    @GetMapping("/{shortCode}/info")
    public ResponseEntity<UrlInfoResponse> getUrlInfo(@PathVariable String shortCode) {
        UrlEntity entity = urlShortenerService.getUrlInfo(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        return ResponseEntity.ok(new UrlInfoResponse(
                entity.getOriginalUrl(),
                entity.getShortCode(),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getAccessCount()
        ));
    }
}