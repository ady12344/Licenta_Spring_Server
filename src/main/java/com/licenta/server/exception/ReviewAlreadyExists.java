package com.licenta.server.exception;

public class ReviewAlreadyExists extends RuntimeException {
    public ReviewAlreadyExists(String message) {
        super(message);
    }
}
