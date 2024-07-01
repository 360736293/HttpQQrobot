package com.httpqqrobot.controller;


import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.utils.SendGetMessage;
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

    @RateLimit(limit = 2)
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

    @RateLimit(limit = 1)
    @RequestMapping("/test")
    public void test(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("hello world");
    }

    //cron 定时属性  秒 分 时 日期 月份 星期几 年份
//    @Scheduled(cron = "* * * * * ?")
//    public void testScheduledTask() {
//
//    }
}