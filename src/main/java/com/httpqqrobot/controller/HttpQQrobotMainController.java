package com.httpqqrobot.controller;


import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.result.Result;
import com.httpqqrobot.result.ResultInfoEnum;
import com.httpqqrobot.utils.RequestHolderUtil;
import com.httpqqrobot.utils.RobotUtil;
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
        RequestHolderUtil.remove();
        log.info("input parameter: {}", json.toJSONString());
        functionHandlerChain.doHandler(json);
        return Result.success(ResultInfoEnum.SUCCESS.getCode(), ResultInfoEnum.SUCCESS.getMsg(), null);
    }
}