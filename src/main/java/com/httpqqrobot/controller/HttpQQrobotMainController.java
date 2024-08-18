package com.httpqqrobot.controller;


import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.result.Result;
import com.httpqqrobot.result.ResultInfoEnum;
import com.httpqqrobot.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@Api(tags = "主流程相关接口")
public class HttpQQrobotMainController {
    @Resource
    private FunctionHandlerChain functionHandlerChain;

    /**
     * 机器人的代理服务器只会请求一个接口，所以处理流程全部在一个接口里，并且无固定的请求体
     * 下面是示例用请求体
    {
        "group_id":"1",
        "sender":{
            "user_id":"1",
            "nickname":"1"
        },
        "message":"1",
        "time":{{$timestamp}}
    }
    **/
    @RateLimit(limit = 2)
    @Authorize(role = "user")
    @PostMapping("/handler")
    @ApiOperation("主流程处理")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 401, message = "未认证"),
            @ApiResponse(code = 403, message = "禁止访问"),
            @ApiResponse(code = 503, message = "服务不可用"),
            @ApiResponse(code = -1, message = "未知失败")
    })
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
