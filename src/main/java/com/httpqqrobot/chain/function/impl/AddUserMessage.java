package com.httpqqrobot.chain.function.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.service.IUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@ChainSequence(0)
public class AddUserMessage implements FunctionAct {

    @Resource
    private IUserMessageService userMessageService;

    @Override
    public void act(JSONObject json) {
        try {
            //将消息记录进数据库中
            UserMessage userMessage = JSONObject.toJavaObject(json, UserMessage.class);
            userMessage.setId(IdUtil.getSnowflakeNextIdStr());
            if (ObjectUtil.isNotEmpty(userMessage.getTime())) {
                userMessage.setTime(LocalDateTimeUtil.format(LocalDateTimeUtil.of(Integer.parseInt(userMessage.getTime()) * 1000L), "yyyy-MM-dd HH:mm:ss"));
            }
            if (ObjectUtil.isNotEmpty(userMessage.getSender())) {
                userMessage.setSenderUserId(userMessage.getSender().getUserId());
                userMessage.setSenderNickname(userMessage.getSender().getNickname());
                userMessage.setSenderCard(userMessage.getSender().getCard());
            }
            userMessageService.save(userMessage);
        } catch (Exception e) {
            log.info("记录用户消息异常: {}", e.getMessage());
        }
    }
}
