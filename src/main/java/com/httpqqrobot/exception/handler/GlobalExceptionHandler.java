package com.httpqqrobot.exception.handler;

import com.alibaba.fastjson2.JSONObject;
import com.httpqqrobot.exception.AuthorizeException;
import com.httpqqrobot.exception.RateLimitException;
import com.httpqqrobot.result.Result;
import com.httpqqrobot.result.ResultInfoEnum;
import com.httpqqrobot.utils.RequestHolderUtil;
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
        JSONObject json = RequestHolderUtil.get();
        RequestHolderUtil.remove();
        log.error("RateLimit Error: {}", json.toJSONString());
        return Result.fail(ResultInfoEnum.SERVICEUNAVAILABLE.getCode(), ResultInfoEnum.SERVICEUNAVAILABLE.getMsg(), "RateLimit Error");
    }

    @ExceptionHandler(AuthorizeException.class)
    @ResponseBody
    public Result handleAuthorizeException(AuthorizeException e) {
        JSONObject json = RequestHolderUtil.get();
        RequestHolderUtil.remove();
        log.error("Authorize Error: {}", json.toJSONString());
        return Result.fail(ResultInfoEnum.FORBIDDEN.getCode(), ResultInfoEnum.FORBIDDEN.getMsg(), "Authorize Error");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        JSONObject json = RequestHolderUtil.get();
        RequestHolderUtil.remove();
        log.error("Unknow Error: {}", json);
        log.error("Cause: ", e);
        return Result.fail(ResultInfoEnum.UNKNOWFAIL.getCode(), ResultInfoEnum.UNKNOWFAIL.getMsg(), null);
    }
}
