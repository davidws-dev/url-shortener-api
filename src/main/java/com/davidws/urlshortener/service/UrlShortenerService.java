package com.davidws.urlshortener.service;

import com.davidws.urlshortener.entity.UrlEntity;
import com.davidws.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlRepository urlRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    private final Random random = new Random();

    // Gerar código único
    private String generateUniqueShortCode() {
        String shortCode;
        do {
            StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
            for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            shortCode = sb.toString();
        } while (urlRepository.existsByShortCode(shortCode));
        return shortCode;
    }

    // Encurtar URL (sem data de expiração)
    public UrlEntity shortenUrl(String originalUrl) {
        return shortenUrl(originalUrl, null);
    }

    // Encurtar URL com data de expiração opcional
    public UrlEntity shortenUrl(String originalUrl, LocalDateTime expiresAt) {
        String shortCode = generateUniqueShortCode();
        UrlEntity urlEntity = new UrlEntity(originalUrl, shortCode, LocalDateTime.now(), expiresAt);
        return urlRepository.save(urlEntity);
    }

    // Buscar URL original pelo código, incrementando contador
    public Optional<String> getOriginalUrl(String shortCode) {
        Optional<UrlEntity> urlOpt = urlRepository.findByShortCode(shortCode);
        if (urlOpt.isPresent()) {
            UrlEntity url = urlOpt.get();
            // Verificar se expirou
            if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
                return Optional.empty();
            }
            url.incrementAccessCount();
            urlRepository.save(url);
            return Optional.of(url.getOriginalUrl());
        }
        return Optional.empty();
    }

    // Buscar informações da URL (para estatísticas)
    public Optional<UrlEntity> getUrlInfo(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }
}