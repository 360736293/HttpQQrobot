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
                .get("https://cse.google.com/cse/element/v1")
                .form("num", "5")
                .form("cselibv", "8fa85d58e016b414")
                .form("cx", "17bf6301118d143f2")
                .form("q", content)
                .form("safe", "on")
                .form("cse_tok", "AB-tC_7ywA3y29M-is78iqBGBFGt:1736575153477")
                .form("callback", "google.search.cse.api14638")
                .form("rurl", "https://www.sougood.top/free/833/%23gsc.tab=0");
        if (ObjectUtil.isNotEmpty(AppConstant.proxyIP) && ObjectUtil.isNotEmpty(AppConstant.proxyPort)) {
            httpRequest.setHttpProxy(AppConstant.proxyIP, AppConstant.proxyPort);
        }
        String response = httpRequest.execute().body();
        response = response.replace("/*O_o*/", "");
        response = response.replace("google.search.cse.api14638({", "");
        response = response.replace("});", "");
        response = "{" + response + "}";
        JSONObject json = JSONObject.parseObject(response);
        List<JSONObject> results = json.getJSONArray("results").toJavaList(JSONObject.class);
        for (JSONObject result : results) {
            res.add(result.getString("contentNoFormatting"));
        }
        log.info("response information: {}", res);
        return res;
    }
}
