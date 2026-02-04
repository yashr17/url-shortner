package com.project.urlshortner.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.project.urlshortner.dto.RequestInfo;
import com.project.urlshortner.entity.Url;
import com.project.urlshortner.exception.GoneException;
import com.project.urlshortner.exception.RedirectException;
import com.project.urlshortner.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final CachingService cachingService;
    private final UrlRepository urlRepository;
    private final AnalyticsService analyticsService;

    public String getOriginalUrl(String shortcode, HttpServletRequest request) {
        String cachedUrl = cachingService.getCachedUrl(shortcode);
        if (cachedUrl != null) {
            saveAnalytics(shortcode, request);
            return cachedUrl;
        }
        Url urlEntity = queryDatabaseForUrl(shortcode);
        if(urlEntity.isExpired()) {
            throw new GoneException("URL has expired for shortcode: " + shortcode);
        }
        String originalUrl = urlEntity.getOriginalUrl();
        cachingService.cacheUrl(urlEntity);
        saveAnalytics(shortcode, request);
        return originalUrl;
    }

    private Url queryDatabaseForUrl(String shortcode) {
        return urlRepository.findByShortCodeAndIsActive(shortcode, Boolean.TRUE)
                .orElseThrow(() -> new RedirectException("URL not found or inactive for shortcode: " + shortcode));
    }

    private void saveAnalytics(String shortcode, HttpServletRequest request) {
        RequestInfo requestInfo = buildRequestInfo(request);
        analyticsService.trackClick(shortcode, requestInfo);
    }

    private RequestInfo buildRequestInfo(HttpServletRequest request) {
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");
        String country = getCountryFromIp(ipAddress);
        return RequestInfo.builder()
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .referrer(referrer)
                .country(country)
                .build();
    }

    private String getCountryFromIp(String ipAddress) {
        // Placeholder for actual IP to country resolution logic
        return null; // Defaulting to null for demonstration purposes
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
