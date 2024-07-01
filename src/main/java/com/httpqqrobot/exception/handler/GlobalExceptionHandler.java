package com.httpqqrobot.exception.handler;

import com.httpqqrobot.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitException.class)
    @ResponseBody
    public String handleNeedLoginException(RateLimitException e) {
        log.error("Rate Limit Exception: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Exception e) {
        log.error("Unknown Exception: {}", e.getMessage());
        return e.getMessage();
    }
}
