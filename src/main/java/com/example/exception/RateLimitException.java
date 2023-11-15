package com.example.exception;

public class RateLimitException extends RuntimeException {
    public RateLimitException(String msg) {
        super(msg);
    }
}
