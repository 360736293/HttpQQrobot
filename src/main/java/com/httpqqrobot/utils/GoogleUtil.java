package com.httpqqrobot.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
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
        String response = HttpRequest
                .get("https://cse.google.com/cse/element/v1")
                .form("num", "5")
                .form("cselibv", "8fa85d58e016b414")
                .form("cx", "17bf6301118d143f2")
                .form("q", content)
                .form("safe", "on")
                .form("cse_tok", "AB-tC_7cLI6GUH-nvzwIc876uHps%3A1736512213901")
                .form("callback", "google.search.cse.api9564")
                .setHttpProxy("127.0.0.1",7890)
                .execute()
                .body();
        response = response.replace("/*O_o*/", "");
        response = response.replace("google.search.cse.api9564({", "");
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
