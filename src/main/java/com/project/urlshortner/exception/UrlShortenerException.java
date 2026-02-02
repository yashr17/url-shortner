package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class UrlShortenerException extends RuntimeException {
    private final HttpStatus httpStatus;

    public UrlShortenerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}