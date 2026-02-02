package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends UrlShortenerException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}