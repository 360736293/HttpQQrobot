package com.example.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.UserMessage;
import com.example.mapper.UserMessageMapper;
import com.example.service.IUserMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {
    @Resource
    private UserMessageMapper userMessageMapper;

    @Override
    public void addUserMessage(String qqGroup, String qqNumber, String nickName, String content, Date timeStamp) {
        userMessageMapper.addUserMessage(qqGroup, qqNumber, nickName, content, timeStamp);
    }

    @Override
    public List<UserMessage> getTodayUserSpeakRank(String qqGroup, String date) {
        return userMessageMapper.getTodayUserSpeakRank(qqGroup, date);
    }

    @Override
    public List<String> getTodayUserSpeakContent(String qqGroup, String date) {
        return userMessageMapper.getTodayUserSpeakContent(qqGroup, date);
    }
}
