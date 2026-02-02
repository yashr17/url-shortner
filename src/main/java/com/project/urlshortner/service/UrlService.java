package com.project.urlshortner.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.urlshortner.dto.ShortenRequest;
import com.project.urlshortner.dto.ShortenResponse;
import com.project.urlshortner.entity.ApiKey;
import com.project.urlshortner.entity.Url;
import com.project.urlshortner.repository.UrlRepository;
import com.project.urlshortner.util.ShortCodeGenerator;
import com.project.urlshortner.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final ValidationUtil validationUtil;
    private final RateLimitService rateLimitService;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlRepository urlRepository;
    private final CachingService cachingService;

    @Value("${app.base-url:http://shortly.url}")
    private String baseUrl;

    @Value("${app.default-expiry-days:7}")
    private int defaultExpiryDays;

    public ShortenResponse shortenUrl(ShortenRequest request, String apiKey) {

        ApiKey validatedApiKey = validateRequest(request, apiKey);

        String shortCode = shortCodeGenerator.generate(request.getCustomAlias());

        Url savedUrl = saveUrl(request, shortCode, validatedApiKey);

        cachingService.cacheUrl(savedUrl);

        return buildShortenResponse(savedUrl);
    }

    private ApiKey validateRequest(ShortenRequest request, String apiKey) {
        validationUtil.validateUrl(request.getUrl());
        ApiKey apiKeyEntity = validationUtil.validateApiKey(apiKey);
        rateLimitService.checkRateLimit(apiKey, apiKeyEntity.getRateLimitPerHour());
        return apiKeyEntity;
    }

    private Url saveUrl(ShortenRequest request, String shortCode, ApiKey apiKey) {
        Url url = new Url();
        url.setShortCode(shortCode);
        url.setOriginalUrl(request.getUrl());
        url.setApiKey(apiKey);
        url.setExpiresAt(calculateExpiryDate(request.getExpiryDays()));
        return urlRepository.save(url);
    }

    private ShortenResponse buildShortenResponse(Url savedUrl) {
        String shortUrl = baseUrl + "/" + savedUrl.getShortCode();
        ShortenResponse response = ShortenResponse.builder()
                .originalUrl(savedUrl.getOriginalUrl())
                .shortUrl(shortUrl)
                .shortCode(savedUrl.getShortCode())
                .createdAt(savedUrl.getCreatedAt())
                .expiresAt(savedUrl.getExpiresAt())
                .build();
        return response;
    }

    private LocalDateTime calculateExpiryDate(Integer expiryDays) {
        if (expiryDays == null) {
            expiryDays = defaultExpiryDays; // default expiry of 7 days
        }
        return LocalDateTime.now().plusDays(expiryDays);
    }
}
