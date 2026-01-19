package com.project.urlshortner.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.urlshortner.dto.ShortenRequest;
import com.project.urlshortner.dto.ShortenResponse;
import com.project.urlshortner.util.ShortCodeGenerator;

@Service
public class UrlService {

    @Autowired
    private ShortCodeGenerator shortCodeGenerator;

    @Value("${app.base-url:http://shortly.url}")
    private String baseUrl;

    public ShortenResponse shortenUrl(ShortenRequest request) {
        String shortCode = shortCodeGenerator.generate();
        String shortUrl = baseUrl + "/" + shortCode;
        LocalDateTime exp = LocalDateTime.now(ZoneId.of("Asia/Kolkata")).plusMinutes(10);
        ShortenResponse response = ShortenResponse.builder()
                .originalUrl(request.getUrl())
                .shortUrl(shortUrl)
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")))
                .expiresAt(exp)
                .build();
        return response;
    }
}
