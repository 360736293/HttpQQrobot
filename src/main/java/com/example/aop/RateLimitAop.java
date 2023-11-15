package com.example.aop;

import cn.hutool.core.util.ObjectUtil;
import com.example.annotation.RateLimit;
import com.example.exception.RateLimitException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Aspect
@Component
public class RateLimitAop {
    private final ConcurrentHashMap<String, Integer> tokens = new ConcurrentHashMap<>();

    public RateLimitAop() {
        //启动令牌桶重置线程
        ScheduledExecutorService producer = Executors.newScheduledThreadPool(1);
        producer.scheduleAtFixedRate(
                //重置令牌桶
                tokens::clear,
                //等待1秒执行第一次
                1,
                //每隔1秒执行一次
                1,
                java.util.concurrent.TimeUnit.SECONDS
        );
    }

    @Before("@annotation(com.example.annotation.RateLimit)")
    public void before(JoinPoint joinPoint) {
        Class<?> aClass = joinPoint.getSignature().getDeclaringType();
        for (Method method : aClass.getMethods()) {
            if (ObjectUtil.equal(joinPoint.getSignature().getName(), method.getName())) {
                if (method.isAnnotationPresent(com.example.annotation.RateLimit.class)) {
                    RateLimit rateLimit = method.getAnnotation(RateLimit.class);
                    String methodName = method.getName();
                    int limit = rateLimit.limit();
                    int token = tokens.getOrDefault(methodName, 0);
                    //因为不清楚每个接口各自的令牌数量，所以为了方便重置令牌线程，这里从小到大统计
                    if (token < limit) {
                        //令牌桶中还有令牌，加上本次请求消耗的令牌
                        tokens.put(methodName, token + 1);
                    } else {
                        //令牌桶中没有令牌，抛出异常
                        throw new RateLimitException("Rate Limit Exception");
                    }
                }
            }
        }
    }

}
