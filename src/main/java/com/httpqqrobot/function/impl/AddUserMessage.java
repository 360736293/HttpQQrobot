package com.httpqqrobot.function.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.function.FunctionAct;
import com.httpqqrobot.service.IUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Slf4j
@ChainSequence(3)
public class AddUserMessage implements FunctionAct {
    @Resource
    IUserMessageService userMessageService;

    public void act(JSONObject json, HttpServletResponse resp) {
        try {
            //消息记录到数据库中
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
            log.info("记录消息异常: {}", e.getMessage());
        }
    }
}
