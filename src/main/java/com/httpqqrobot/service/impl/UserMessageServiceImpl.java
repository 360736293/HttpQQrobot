package com.httpqqrobot.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.mapper.UserMessageMapper;
import com.httpqqrobot.service.IUserMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {
    @Resource
    private UserMessageMapper userMessageMapper;

    @Override
    public List<UserMessage> getTodayUserSpeakRank(String qqGroup, String date) {
        return userMessageMapper.getTodayUserSpeakRank(qqGroup, date);
    }

    @Override
    public List<String> getTodayUserSpeakContent(String qqGroup, String date) {
        return userMessageMapper.getTodayUserSpeakContent(qqGroup, date);
    }
}
