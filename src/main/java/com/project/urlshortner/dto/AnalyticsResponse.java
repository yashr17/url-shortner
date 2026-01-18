package com.project.urlshortner.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsResponse {
    private String shortCode;
    private String originalUrl;
    private Long totalClicks;
    private LocalDateTime createdAt;
    private List<ClickData> recentClicks;
    private Map<String, Long> clicksByDate;
    private Map<String, Long> clicksByCountry;
    
    @Data
    @Builder
    public static class ClickData {
        private LocalDateTime clickedAt;
        private String ipAddress;
        private String userAgent;
        private String referrer;
        private String country;
    }
}