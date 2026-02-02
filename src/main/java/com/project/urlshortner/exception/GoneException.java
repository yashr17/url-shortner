package com.project.urlshortner.exception;

import org.springframework.http.HttpStatus;

public class GoneException extends UrlShortenerException {
    public GoneException(String message) {
        super(message, HttpStatus.GONE);
    }

}
