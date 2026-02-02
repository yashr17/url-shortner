package com.project.urlshortner.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.urlshortner.dto.ErrorResponse;

@RestControllerAdvice
public class UrlShortnerControllerAdvice {

    private static final String UNKNOWN_EXCEPTION_MESSAGE = "Unknown exception occurred";

    @ExceptionHandler(UrlShortenerException.class)
    public ResponseEntity<ErrorResponse> handleUrlShortenerException(
            UrlShortenerException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .httpStatus(ex.getHttpStatus())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        // Map common IllegalArgumentException messages to appropriate HTTP status codes
        HttpStatus status = HttpStatus.BAD_REQUEST; // Default to Bad Request
        String message = ex.getMessage();

        if (message == null) {
            message = UNKNOWN_EXCEPTION_MESSAGE + " - Illegal argument exception";
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
            .httpStatus(status)
            .message(message)
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        // Map common RuntimeException messages to appropriate HTTP status codes
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Default to Internal Server Error
        String message = ex.getMessage();

        if (message == null) {
            message = UNKNOWN_EXCEPTION_MESSAGE + " - Runtime exception";
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
            .httpStatus(status)
            .message(message)
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            .message(UNKNOWN_EXCEPTION_MESSAGE + " - Internal server error")
            .path(request.getRequestURI())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
