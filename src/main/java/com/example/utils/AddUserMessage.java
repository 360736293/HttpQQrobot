package com.example.utils;

import com.example.service.IUserMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class AddUserMessage {
    @Resource
    IUserMessageService userMessageService;

    public void act(String qqGroup, String qqNumber, String nickName, String content, Date timeStamp) {
        userMessageService.addUserMessage(qqGroup, qqNumber, nickName, content, timeStamp);
    }
}
