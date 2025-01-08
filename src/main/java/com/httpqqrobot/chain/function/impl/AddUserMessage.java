package com.httpqqrobot.chain.function.impl;

import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.service.IUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class AddUserMessage implements FunctionAct {

    @Resource
    private IUserMessageService userMessageService;

    @Override
    public void act(UserMessage userMessage) {
        try {
            //将用户消息记录进数据库中
//            String group_id = json.getString("group_id");
//            String qq = json.getJSONObject("sender").getString("user_id");
//            String nickName = json.getJSONObject("sender").getString("nickname");
//            String message = json.getString("message");
//            Date date = new Date(json.getLong("time") * 1000);
//            UserMessage userMessage = new UserMessage();
//            userMessage.setId(IdUtil.getSnowflakeNextIdStr());
//            userMessage.setQqGroup(group_id);
//            userMessage.setQqNumber(qq);
//            userMessage.setQqName(nickName);
//            userMessage.setContent(message);
//            userMessage.setDate(date);
//            userMessageService.save(userMessage);
        } catch (Exception e) {
            log.info("记录用户消息异常: {}", e.getMessage());
        }
    }
}
