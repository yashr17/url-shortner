package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends UrlShortenerException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}