package com.httpqqrobot.chain.function.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.service.IUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Slf4j
@ChainSequence(0)
public class AddUserMessage implements FunctionAct {

    @Resource
    private IUserMessageService userMessageService;

    @Authorize(role = {"user", "admin"})
    @Override
    public void act(JSONObject json, HttpServletResponse resp) {
        try {
            //推送待记录的消息到消息队列中
            String group_id = json.getString("group_id");
            String qq = json.getJSONObject("sender").getString("user_id");
            String nickName = json.getJSONObject("sender").getString("nickname");
            String message = json.getString("message");
            Date date = new Date(json.getLong("time") * 1000);
            UserMessage userMessage = new UserMessage();
            userMessage.setId(IdUtil.getSnowflakeNextIdStr());
            userMessage.setQqGroup(group_id);
            userMessage.setQqNumber(qq);
            userMessage.setQqName(nickName);
            userMessage.setContent(message);
            userMessage.setDate(date);
            userMessageService.save(userMessage);
        } catch (Exception e) {
            log.info("记录消息推送异常: {}", e.getMessage());
        }
    }
}
