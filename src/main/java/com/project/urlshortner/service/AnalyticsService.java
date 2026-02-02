package com.project.urlshortner.service;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.urlshortner.dto.RequestInfo;
import com.project.urlshortner.entity.Url;
import com.project.urlshortner.entity.UrlAnalytics;
import com.project.urlshortner.repository.UrlAnalyticsRepository;
import com.project.urlshortner.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UrlRepository urlRepository;
    private final UrlAnalyticsRepository urlAnalyticsRepository;

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
                .country(null)
                .build();
        urlAnalyticsRepository.save(analytics);
    }
}
