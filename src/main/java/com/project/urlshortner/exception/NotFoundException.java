package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends UrlShortenerException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}