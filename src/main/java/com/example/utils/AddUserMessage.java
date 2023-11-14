package com.example.utils;

import cn.hutool.core.util.IdUtil;
import com.example.entity.UserMessage;
import com.example.service.IUserMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class AddUserMessage {
    @Resource
    IUserMessageService userMessageService;

    public void act(String qqGroup, String qqNumber, String nickName, String content, Date timeStamp) {
        UserMessage userMessage = new UserMessage();
        userMessage.setId(IdUtil.getSnowflakeNextIdStr());
        userMessage.setQqGroup(qqGroup);
        userMessage.setQqNumber(qqNumber);
        userMessage.setQqName(nickName);
        userMessage.setContent(content);
        userMessage.setDate(timeStamp);
        userMessageService.save(userMessage);
    }
}
