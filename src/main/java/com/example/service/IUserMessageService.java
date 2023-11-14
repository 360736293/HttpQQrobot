package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UserMessage;

import java.util.Date;
import java.util.List;

public interface IUserMessageService extends IService<UserMessage> {
    void addUserMessage(String qqGroup, String qqNumber,String nickName, String content, Date timeStamp);

    List<UserMessage> getTodayUserSpeakRank(String qqGroup, String date);

    List<String> getTodayUserSpeakContent(String qqGroup, String date);
}
