package com.httpqqrobot.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.exception.AuthorizeException;
import com.httpqqrobot.utils.RedisUtils;
import com.httpqqrobot.utils.RequestHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthorizeAop {

    @Before("@annotation(com.httpqqrobot.annotation.Authorize)")
    public void before(JoinPoint joinPoint) {
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            if (method.isAnnotationPresent(Authorize.class)) {
                Authorize authorize = method.getAnnotation(Authorize.class);
                String[] role = authorize.role();
                JSONObject json = RequestHolder.get();
                //获取到user_id
                String userId = json.getJSONObject("sender").getString("user_id");
                String userRole = RedisUtils.getStr("UserId:" + userId);
                //判断是否包含用户角色
                if (!ObjectUtil.contains(role, userRole)) {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            throw new AuthorizeException("Authorize Exception");
        }
    }
}
