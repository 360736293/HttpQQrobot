package com.httpqqrobot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.httpqqrobot.entity.UserMessage;

import java.util.List;

public interface IUserMessageService extends IService<UserMessage> {

    List<UserMessage> getTodayUserSpeakRank(String qqGroup, String date);

    List<String> getTodayUserSpeakContent(String qqGroup, String date);
}
