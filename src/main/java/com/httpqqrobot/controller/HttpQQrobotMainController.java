package com.httpqqrobot.controller;


import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.utils.SendGetMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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
//        "time":10086
//    }
    @RateLimit(limit = 5)
    @RequestMapping("/handler")
    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        try {
            JSONObject json = SendGetMessage.getMessage(req);
            log.info("input parameter: {}", json.toJSONString());
            functionHandlerChain.doHandler(json, resp);
        } catch (Exception e) {
            log.info("handler异常: {}", e.getMessage());
        }
    }
}
