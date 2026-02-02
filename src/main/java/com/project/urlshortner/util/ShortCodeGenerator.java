package com.project.urlshortner.util;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.urlshortner.exception.ConflictException;
import com.project.urlshortner.repository.UrlRepository;

@Component
public class ShortCodeGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private UrlRepository urlRepository;

    @Value("${app.short-code-length:7}")
    private int shortCodeLength;
    
    public String generate(String customAlias) {
        if (isValidCustomAlias(customAlias)) {
            if (urlRepository.existsByShortCode(customAlias)) {
                throw new ConflictException("Custom alias already in use");
            }
            return customAlias;
        } else {
            // log error saying invalid custom alias
        }

        int attempts = 0;
        final int maxAttempts = 10;
        String shortCode;
        do {
            shortCode = generateRandomShortCode();
            attempts++;

            if (attempts > maxAttempts) {
                throw new RuntimeException("Failed to generate unique short code after " + maxAttempts + " attempts");
            }
        } while (urlRepository.existsByShortCode(shortCode));
        return shortCode;
    }

    private String generateRandomShortCode() {
        StringBuilder code = new StringBuilder(shortCodeLength);
        for (int i = 0; i < shortCodeLength; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
    
    public boolean isValidCustomAlias(String alias) {
        return alias != null
                && alias.length() >= 4
                && alias.length() <= 10
                && alias.matches("[a-zA-Z0-9_-]+");
    }
}