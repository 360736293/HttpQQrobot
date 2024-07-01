package com.httpqqrobot.exception;

public class RateLimitException extends RuntimeException {
    public RateLimitException(String msg) {
        super(msg);
    }
}
