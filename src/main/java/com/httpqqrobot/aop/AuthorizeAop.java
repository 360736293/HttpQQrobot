package com.httpqqrobot.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.entity.UserRoleEnum;
import com.httpqqrobot.exception.AuthorizeException;
import com.httpqqrobot.utils.RequestHolderUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@Order(100001)
public class AuthorizeAop {

    @Around("@annotation(com.httpqqrobot.annotation.Authorize)")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            if (method.isAnnotationPresent(Authorize.class)) {
                Authorize authorize = method.getAnnotation(Authorize.class);
                Integer roleValue = authorize.roleValue();
                JSONObject json = RequestHolderUtil.get();
                UserMessage userMessage = JSONObject.parseObject(json.toJSONString(), UserMessage.class, JSONReader.Feature.SupportSmartMatch);
                String userId = userMessage.getUserId();
                Integer userRoleValue = AppConstant.userAuthorityMap.get(userId);
                if (ObjectUtil.isEmpty(userRoleValue)) {
                    userRoleValue = UserRoleEnum.Guest.getRoleValue();
                }
                //判断用户角色权限值是否大于需求角色权限值
                if (userRoleValue >= roleValue) {
                    return joinPoint.proceed();
                } else {
                    throw new AuthorizeException();
                }
            }
            return null;
        } catch (AuthorizeException e) {
            throw new AuthorizeException();
        } catch (Throwable e) {
            log.error("鉴权AOP异常: ", e);
            return null;
        }
    }
}
