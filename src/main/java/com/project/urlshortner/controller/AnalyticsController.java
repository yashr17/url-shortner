package com.project.urlshortner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.urlshortner.dto.AnalyticsResponse;
import com.project.urlshortner.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    public final AnalyticsService analyticsService;

    @GetMapping("/{shortcode}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String shortcode, @RequestHeader("X-API-Key") String apiKey) {
        AnalyticsResponse analyticsResponse = analyticsService.getAnalytics(shortcode, apiKey);
        return ResponseEntity.ok(analyticsResponse);
    }
}
