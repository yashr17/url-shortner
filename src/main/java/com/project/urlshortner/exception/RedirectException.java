package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class RedirectException extends UrlShortenerException {
    public RedirectException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
