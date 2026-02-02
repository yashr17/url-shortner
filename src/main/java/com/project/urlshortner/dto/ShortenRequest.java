package com.project.urlshortner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenRequest {
    private String url;
    private String customAlias; // Optional custom short code
    private Integer expiryDays; // Optional expiry
}