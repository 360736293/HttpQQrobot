package com.httpqqrobot.exception.handler;

import com.httpqqrobot.exception.AuthorizeException;
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
        //限流异常要给前台返回
        log.error("Rate Limit Exception: {}", e.getMessage());
        return Result.fail(ResultInfoEnum.SERVICEUNAVAILABLE.getCode(), ResultInfoEnum.SERVICEUNAVAILABLE.getMsg(), e.getMessage());
    }

    @ExceptionHandler(AuthorizeException.class)
    @ResponseBody
    public Result handleAuthorizeException(AuthorizeException e) {
        //鉴权异常要给前台返回
        log.error("Authorize Exception: {}", e.getMessage());
        return Result.fail(ResultInfoEnum.FORBIDDEN.getCode(), ResultInfoEnum.FORBIDDEN.getMsg(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        log.error("Unknown Exception: {}", e.getMessage());
        return Result.fail(ResultInfoEnum.UNKNOWFAIL.getCode(), ResultInfoEnum.UNKNOWFAIL.getMsg(), null);
    }
}
