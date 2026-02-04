package com.project.urlshortner.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.urlshortner.dto.AnalyticsResponse;
import com.project.urlshortner.dto.AnalyticsResponse.ClickData;
import com.project.urlshortner.dto.RequestInfo;
import com.project.urlshortner.entity.Url;
import com.project.urlshortner.entity.UrlAnalytics;
import com.project.urlshortner.repository.UrlAnalyticsRepository;
import com.project.urlshortner.repository.UrlRepository;
import com.project.urlshortner.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UrlRepository urlRepository;
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final ValidationUtil validationUtil;

    @Value("${app.base-url:http://shortly.url}")
    private String baseUrl;

    @Value("${app.analytics.recent-clicks-limit:10}")
    private int recentClicksLimit;

    @Async("analyticsExecutor")
    @Transactional
    public void trackClick(String shortcode, RequestInfo requestInfo) {
        urlRepository.incrementClickCount(shortcode);
        Url url = urlRepository.findByShortCodeAndIsActive(shortcode, Boolean.TRUE)
                .orElse(null);
        if (url == null) {
            return;
        }
        UrlAnalytics analytics = UrlAnalytics.builder()
                .urlId(url.getId())
                .ipAddress(requestInfo.getIpAddress())
                .userAgent(requestInfo.getUserAgent())
                .referrer(requestInfo.getReferrer())
                .country(requestInfo.getCountry())
                .build();
        urlAnalyticsRepository.save(analytics);
    }

    @Transactional(readOnly = true)
    public AnalyticsResponse getAnalytics(String shortcode, String apiKey) {
        Url url = validationUtil.validateApiKeyAndShortcode(apiKey, shortcode);
        return buildAnalyticsResponse(url);
    }

    private AnalyticsResponse buildAnalyticsResponse(Url url) {
        AnalyticsResponse analyticsResponse = AnalyticsResponse.builder()
                .shortCode(url.getShortCode())
                .originalUrl(url.getOriginalUrl())
                .shortUrl(baseUrl + "/" + url.getShortCode())
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .totalClicks(url.getClickCount())
                .isActive(url.getIsActive())
                .recentClicks(getRecentClicks(url.getId()))
                .clicksByDate(getClicksByDate(url.getId()))
                .clicksByCountry(getClicksByCountry(url.getId()))
                .build();
        return analyticsResponse;
    }

    private Map<String, Long> getClicksByCountry(Long id) {
        return urlAnalyticsRepository.getClicksByCountry(id)
                .stream()
                .collect(Collectors.toMap(
                        row -> row[0] != null ? row[0].toString() : "Unknown",
                        row -> (Long) row[1]));
    }

    private Map<String, Long> getClicksByDate(Long id) {
        return urlAnalyticsRepository.getClicksByDate(id)
                .stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> ((BigInteger) row[1]).longValue()));
    }

    private List<ClickData> getRecentClicks(Long urlId) {
        return urlAnalyticsRepository.findTop10ByUrlIdOrderByClickedAtDesc(urlId)
                .stream()
                .map(a -> ClickData.builder()
                        .clickedAt(a.getClickedAt())
                        .ipAddress(a.getIpAddress())
                        .userAgent(a.getUserAgent())
                        .referrer(a.getReferrer())
                        .country(a.getCountry())
                        .build())
                .collect(Collectors.toList());

    }
}
