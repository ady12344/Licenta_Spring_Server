package com.licenta.server.exception;

public class ReviewNotFound extends RuntimeException {
    public ReviewNotFound(String message) {
        super(message);
    }
}
