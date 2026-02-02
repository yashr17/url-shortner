package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends UrlShortenerException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}