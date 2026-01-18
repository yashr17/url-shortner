package com.project.urlshortner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenRequest {
    @NotBlank(message = "URL is required")
    @Pattern(regexp = "^https?://.*", message = "Invalid URL format")
    private String url;
    
    private String customAlias; // Optional custom short code
    
    private Integer expiryDays; // Optional expiry
}