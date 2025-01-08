package com.httpqqrobot.controller;


import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.result.Result;
import com.httpqqrobot.result.ResultInfoEnum;
import com.httpqqrobot.utils.RequestHolderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class HttpQQrobotMainController {
    @Resource
    private FunctionHandlerChain functionHandlerChain;

    @RateLimit(limit = 5)
    @PostMapping("/handler")
    public Result handler(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = RequestHolderUtil.get();
        log.info("input parameter: {}", json.toJSONString());
        UserMessage userMessage = JSONObject.toJavaObject(json, UserMessage.class);
        RequestHolderUtil.remove();
        userMessage.setId(IdUtil.getSnowflakeNextIdStr());
        userMessage.setTime(LocalDateTimeUtil.format(LocalDateTimeUtil.of(Integer.parseInt(userMessage.getTime()) * 1000L), "yyyy-MM-dd HH:mm:ss"));
        functionHandlerChain.doHandler(userMessage);
        return Result.success(ResultInfoEnum.SUCCESS.getCode(), ResultInfoEnum.SUCCESS.getMsg(), null);
    }
}
