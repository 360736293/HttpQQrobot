package com.httpqqrobot.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHolderUtil {

    private static final ConcurrentHashMap<String, ThreadLocal<JSONObject>> threadLocalMap = new ConcurrentHashMap<>();

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
        threadLocalMap.remove(Thread.currentThread().getName());
    }

    public static JSONObject get() {
        ThreadLocal<JSONObject> threadLocal = Optional.ofNullable(
                threadLocalMap.get(Thread.currentThread().getName())
        ).orElse(new ThreadLocal<>());
        return threadLocal.get();
    }
}
