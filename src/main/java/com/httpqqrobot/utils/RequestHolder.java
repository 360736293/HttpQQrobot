package com.httpqqrobot.utils;

import com.alibaba.fastjson.JSONObject;

public class RequestHolder {
    private static final ThreadLocal<JSONObject> threadLocal = new ThreadLocal<>();

    public static void add(JSONObject json) {
        threadLocal.set(json);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static JSONObject get() {
        return threadLocal.get();
    }
}
