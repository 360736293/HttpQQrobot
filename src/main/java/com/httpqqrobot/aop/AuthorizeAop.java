package com.httpqqrobot.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.exception.AuthorizeException;
import com.httpqqrobot.utils.RequestHolder;
import com.httpqqrobot.utils.SendGetMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(100002)
public class AuthorizeAop {

    @Before("@annotation(com.httpqqrobot.annotation.Authorize)")
    public void before(JoinPoint joinPoint) {
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            if (method.isAnnotationPresent(Authorize.class)) {
                Authorize authorize = method.getAnnotation(Authorize.class);
                String role = authorize.role();
                Object[] args = joinPoint.getArgs();
                for (Object arg : args) {
                    if (arg instanceof HttpServletRequest) {
                        HttpServletRequest req = (HttpServletRequest) arg;
                        JSONObject json = SendGetMessage.getMessage(req);
                        //获取到user_id
                        String userId = json.getJSONObject("sender").getString("user_id");
                        String userRole = AppConstant.userAuthorityMap.get(userId);
                        //TODO 暂时角色仅判断相等
                        if (ObjectUtil.notEqual(role, userRole)) {
                            throw new RuntimeException();
                        }
                        //请求结果存储到ThreadLocal里
                        RequestHolder.add(json);
                    }
                }
            }
        } catch (Exception e) {
            throw new AuthorizeException("Authorize Exception");
        }
    }
}
