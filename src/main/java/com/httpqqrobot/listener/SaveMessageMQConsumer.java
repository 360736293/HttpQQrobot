package com.httpqqrobot.listener;

import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.service.IUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(topic = "httpqqrobot-savemessage-topic", consumerGroup = "httpqqrobot-consumer-group")
public class SaveMessageMQConsumer implements RocketMQListener<String> {

    @Resource
    private IUserMessageService userMessageService;

    @Override
    public void onMessage(String s) {
        try {
            //记录消息到数据库中
            UserMessage userMessage = JSONObject.parseObject(s).toJavaObject(UserMessage.class);
            userMessageService.save(userMessage);
        } catch (Exception e) {
            log.info("记录消息异常: {}", e.getMessage());
        }
    }
}
