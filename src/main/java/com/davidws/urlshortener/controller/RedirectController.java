package com.davidws.urlshortener.controller;

import com.davidws.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class RedirectController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    private static final List<String> EXCLUDED = List.of(
            "index.html", "swagger-ui", "v3", "api", "favicon.ico"
    );

    @GetMapping("/{shortCode:[a-zA-Z0-9]{4,12}}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        if (EXCLUDED.contains(shortCode)) {
            return ResponseEntity.notFound().build();
        }
        Optional<String> originalUrlOpt = urlShortenerService.getOriginalUrl(shortCode);
        if (originalUrlOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL não encontrada ou expirada");
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrlOpt.get()))
                .build();
    }
}