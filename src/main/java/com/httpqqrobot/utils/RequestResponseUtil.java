package com.httpqqrobot.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class RequestResponseUtil {
    public static JSONObject getRequestMessage(HttpServletRequest req) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        return JSONObject.parseObject(sb.toString());
    }

    public static void sendResponseMessage(JSONObject answer, HttpServletResponse resp) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
        bw.write(answer.toString());
        bw.flush();
        bw.close();
    }

}
