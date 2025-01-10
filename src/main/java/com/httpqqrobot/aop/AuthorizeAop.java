package com.httpqqrobot.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.utils.RequestHolderUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthorizeAop {

    @Around("@annotation(com.httpqqrobot.annotation.Authorize)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method.isAnnotationPresent(Authorize.class)) {
            Authorize authorize = method.getAnnotation(Authorize.class);
            String[] role = authorize.role();
            JSONObject json = RequestHolderUtil.get();
            //获取到user_id
            String userId = json.getJSONObject("sender").getString("user_id");
            String userRole = AppConstant.userAuthorityMap.get(userId);
            //判断是否包含用户角色
            if (ObjectUtil.contains(role, userRole)) {
                return joinPoint.proceed();
            }
        }
        return null;
    }
}
