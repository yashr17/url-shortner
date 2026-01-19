package com.project.urlshortner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.project.urlshortner.dto.ShortenRequest;
import com.project.urlshortner.dto.ShortenResponse;
import com.project.urlshortner.service.UrlService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService urlService;
    
    @PostMapping("/v1/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody @Validated ShortenRequest request) {

        ShortenResponse response = urlService.shortenUrl(request);

        return ResponseEntity.ok(response);
    }
}
