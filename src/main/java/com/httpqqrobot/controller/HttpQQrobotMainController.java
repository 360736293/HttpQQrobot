package com.httpqqrobot.controller;


import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.result.Result;
import com.httpqqrobot.result.ResultInfoEnum;
import com.httpqqrobot.utils.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class HttpQQrobotMainController {

    @Resource
    private FunctionHandlerChain functionHandlerChain;

    //    {
    //        "group_id":"1",
    //        "sender":{
    //            "user_id":"1",
    //            "nickname":"1"
    //        },
    //        "message":"1",
    //        "time":{{$timestamp}}
    //    }
    @RateLimit(limit = 2)
    @Authorize(role = "user")
    @RequestMapping("/handler")
    public Result handler(HttpServletRequest req, HttpServletResponse resp) {
        try {
            JSONObject json = RequestHolder.get();
            log.info("input parameter: {}", json.toJSONString());
            functionHandlerChain.doHandler(json, resp);
            RequestHolder.remove();
            return Result.success(ResultInfoEnum.SUCCESS.getCode(), ResultInfoEnum.SUCCESS.getMsg(), null);
        } catch (Exception e) {
            log.info("handler异常: {}", e.getMessage());
            return Result.fail(ResultInfoEnum.UNKNOWFAIL.getCode(), ResultInfoEnum.UNKNOWFAIL.getMsg(), null);
        }
    }
}
