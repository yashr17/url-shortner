package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends UrlShortenerException {
    public TooManyRequestsException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS);
    }
}