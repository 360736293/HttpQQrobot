package com.httpqqrobot.exception.handler;

import com.httpqqrobot.exception.AuthorizeException;
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
    public String handleRateLimitException(RateLimitException e) {
        //限流异常要给前台返回
        log.error("Rate Limit Exception: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(AuthorizeException.class)
    @ResponseBody
    public String handleAuthorizeException(AuthorizeException e) {
        //鉴权异常要给前台返回
        log.error("Authorize Exception: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error("Unknown Exception: {}", e.getMessage());
    }
}
