package com.httpqqrobot.exception.handler;

import com.httpqqrobot.exception.RateLimitException;
import com.httpqqrobot.result.Result;
import com.httpqqrobot.result.ResultInfoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitException.class)
    @ResponseBody
    public Result handleRateLimitException(RateLimitException e) {
        log.error("Error: {}", e.getMessage());
        return Result.fail(ResultInfoEnum.SERVICEUNAVAILABLE.getCode(), ResultInfoEnum.SERVICEUNAVAILABLE.getMsg(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        log.error("Error: {}", e.getMessage());
        return Result.fail(ResultInfoEnum.UNKNOWFAIL.getCode(), ResultInfoEnum.UNKNOWFAIL.getMsg(), null);
    }
}
