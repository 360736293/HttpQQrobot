package com.httpqqrobot.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestHolder {

    private RequestHolder() {
    }

    private static final Map<String, ThreadLocal<JSONObject>> threadLocalMap = new HashMap<>();

    public static void add(JSONObject json) {
        ThreadLocal<JSONObject> threadLocal = Optional.ofNullable(
                threadLocalMap.get(Thread.currentThread().getName())
        ).orElse(new ThreadLocal<>());
        threadLocal.set(json);
        threadLocalMap.put(Thread.currentThread().getName(), threadLocal);
    }

    public static void remove() {
        ThreadLocal<JSONObject> threadLocal = threadLocalMap.get(Thread.currentThread().getName());
        if (ObjectUtil.isNotEmpty(threadLocal)) {
            threadLocal.remove();
        }
    }

    public static JSONObject get() {
        ThreadLocal<JSONObject> threadLocal = Optional.ofNullable(
                threadLocalMap.get(Thread.currentThread().getName())
        ).orElse(new ThreadLocal<>());
        return threadLocal.get();
    }
}
