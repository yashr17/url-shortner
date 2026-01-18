package com.project.urlshortner.util;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShortCodeGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    @Value("${app.short-code-length:7}")
    private int shortCodeLength;
    
    public String generate() {
        StringBuilder sb = new StringBuilder(shortCodeLength);
        for (int i = 0; i < shortCodeLength; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
    
    public boolean isValidCustomAlias(String alias) {
        return alias != null 
            && alias.length() >= 4 
            && alias.length() <= 20 
            && alias.matches("[a-zA-Z0-9_-]+");
    }
}