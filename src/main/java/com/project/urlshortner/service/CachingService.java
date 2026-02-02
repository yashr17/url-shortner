package com.project.urlshortner.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.project.urlshortner.entity.Url;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CachingService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String URL_CACHE_PREFIX = "url:";
    private static final long CACHE_TTL_HOURS = 1;

    public void cacheUrl(Url savedUrl) {
        String shortCode = savedUrl.getShortCode();
        String originalUrl = savedUrl.getOriginalUrl();
        try {
            String cacheKey = URL_CACHE_PREFIX + shortCode;
            redisTemplate.opsForValue().set(cacheKey, originalUrl, CACHE_TTL_HOURS, TimeUnit.HOURS);
        } catch (Exception e) {
            // Don't fail if cache fails - database is source of truth
        }
    }
}
