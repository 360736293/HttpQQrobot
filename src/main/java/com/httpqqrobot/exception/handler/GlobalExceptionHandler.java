package com.httpqqrobot.exception.handler;

import com.httpqqrobot.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitException.class)
    public void handleNeedLoginException(RateLimitException e) {
        log.error("Rate Limit Exception: {}", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void handleNeedLoginException(Exception e) {
        log.error("Unknown Exception: {}", e.getMessage());
    }
}
