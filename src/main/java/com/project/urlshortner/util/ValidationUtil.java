package com.project.urlshortner.util;

import org.springframework.stereotype.Component;

import com.project.urlshortner.entity.ApiKey;
import com.project.urlshortner.entity.Url;
import com.project.urlshortner.exception.BadRequestException;
import com.project.urlshortner.exception.UnauthorizedException;
import com.project.urlshortner.repository.ApiKeyRepository;
import com.project.urlshortner.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

    private final ApiKeyRepository apiKeyRepository;
    private final UrlRepository urlRepository;

    private static final String URL_REGEX = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static final int MIN_URL_LENGTH = 10;
    private static final int MAX_URL_LENGTH = 2048;

    public ApiKey validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new UnauthorizedException("API key is missing");
        }

        return apiKeyRepository.findByApiKeyAndIsActive(apiKey, Boolean.TRUE)
                .orElseThrow(() -> new UnauthorizedException("Invalid or inactive API key"));
    }

    public void validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new BadRequestException("URL cannot be empty");
        }

        // Basic URL format validation
        if (!url.matches(URL_REGEX)) {
            throw new BadRequestException("Invalid URL format");
        }

        // URL length validation
        if (url.length() < MIN_URL_LENGTH) {
            throw new BadRequestException("URL is too short (minimum 10 characters)");
        }
        if (url.length() > MAX_URL_LENGTH) {
            throw new BadRequestException("URL is too long (maximum 2048 characters)");
        }
    }

    public Url validateApiKeyAndShortcode(String apiKey, String shortcode) {
        return urlRepository.findByShortCodeAndApiKey_ApiKeyAndIsActiveTrue(shortcode, apiKey)
                .orElseThrow(() -> new UnauthorizedException("Invalid shortcode and API key combination"));
    }

}
