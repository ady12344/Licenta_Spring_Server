package com.licenta.server.exception;


public class TmdbNotFoundException extends RuntimeException {
    public TmdbNotFoundException(String message) {
        super(message);
    }
}
