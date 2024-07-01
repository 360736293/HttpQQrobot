package com.httpqqrobot.function;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;

public interface FunctionAct {
    public void act(JSONObject json, HttpServletResponse resp);
}
