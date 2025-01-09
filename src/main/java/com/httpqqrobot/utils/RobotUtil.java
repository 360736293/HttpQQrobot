package com.httpqqrobot.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RobotUtil {
    public static JSONObject sendMessage(String subUrl, String body) {
        String url = AppConstant.robotIp + subUrl;
        JSONObject response = JSONObject.parseObject(HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(body)
                .execute()
                .body());
        log.info("response information: {}", response.toJSONString());
        return response;
    }
}
