package com.project.urlshortner.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.urlshortner.dto.ShortenRequest;
import com.project.urlshortner.dto.ShortenResponse;
import com.project.urlshortner.service.UrlService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/v1/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody @Validated ShortenRequest request,
            HttpServletRequest httpRequest) {

        String apiKey = httpRequest.getHeader("X-API-Key");

        ShortenResponse response = urlService.shortenUrl(request, apiKey);

        return ResponseEntity.ok(response);
    }
}
