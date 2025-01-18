package com.httpqqrobot.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
import com.httpqqrobot.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GoogleUtil {

    /**
     * Google搜索
     *
     * @param content
     * @return
     */
    public static List<String> search(String content) {
        List<String> res = new ArrayList<>();
        HttpRequest httpRequest = HttpRequest
                .get("https://www.googleapis.com/customsearch/v1")
                .form("key", AppConstant.googleSearchApikey)
                .form("cx", AppConstant.googleSearchEngineID)
                .form("safe", "active")
                .form("num", "10")
                .form("q", content);
        if (ObjectUtil.isNotEmpty(AppConstant.proxyIP) && ObjectUtil.isNotEmpty(AppConstant.proxyPort)) {
            httpRequest.setHttpProxy(AppConstant.proxyIP, AppConstant.proxyPort);
        }
        String response = httpRequest.execute().body();
        JSONObject json = JSONObject.parseObject(response);
        List<JSONObject> results = json.getJSONArray("items").toJavaList(JSONObject.class);
        for (JSONObject result : results) {
            res.add(result.getString("snippet"));
        }
        log.info("response information: {}", res);
        return res;
    }
}
