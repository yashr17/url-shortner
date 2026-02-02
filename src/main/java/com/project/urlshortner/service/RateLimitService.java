package com.project.urlshortner.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.project.urlshortner.exception.TooManyRequestsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String RATE_LIMIT_CACHE_PREFIX = "rate_limit:";
    private static final long CACHE_TTL_HOURS = 1;

    public void checkRateLimit(String apiKey, int rateLimitPerHour) {

        String cacheKey = RATE_LIMIT_CACHE_PREFIX + apiKey;
        long count = redisTemplate.opsForValue().increment(cacheKey);

        if (count == 1) {
            redisTemplate.expire(cacheKey, CACHE_TTL_HOURS, TimeUnit.HOURS);
        }

        if (count > rateLimitPerHour) {
            throw new TooManyRequestsException("Rate limit exceeded for API key: " + apiKey);
        }
    }
}
