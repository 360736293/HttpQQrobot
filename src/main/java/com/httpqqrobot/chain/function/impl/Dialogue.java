package com.httpqqrobot.chain.function.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.AIRequestBody;
import com.httpqqrobot.utils.RobotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@ChainSequence(1)
public class Dialogue implements FunctionAct {

    @Override
    public void act(JSONObject json) {
        try {
            String message = json.getString("message");
            String[] messageSplit = message.split(" ");
            //不是艾特不回复，messageSplit，0是艾特，1是指令或内容，2是内容
            if (ObjectUtil.notEqual(messageSplit[0], "[CQ:at,qq=" + AppConstant.robotQQ + "]")) {
                return;
            }
            String groupId = json.getString("group_id");
            String messageId = json.getString("message_id");
            String userId = json.getString("user_id");
            switch (messageSplit[1]) {
                case "清除记忆":
                    clearMemory(groupId, messageId, userId);
                    break;
                default:
                    aiTalk(groupId, messageId, userId, messageSplit);
            }
        } catch (Exception e) {
            log.info("对话回复异常: {}", e.getMessage());
        }
    }

    public void clearMemory(String groupId, String messageId, String userId) {
        List<AIRequestBody.Message.MessageContent> chatContextList = AppConstant.chatContext.get(groupId + "-" + userId);
        if (ObjectUtil.isNotEmpty(chatContextList)) {
            chatContextList.clear();
            AppConstant.chatContext.put(groupId + "-" + userId, chatContextList);
        }
        RobotUtil.groupReply(groupId, messageId, "记忆已清除");
    }

    public void aiTalk(String groupId, String messageId, String userId, String[] messageSplit) {
        String messageContent = messageSplit[1];
        JSONObject aiAnswer = RobotUtil.sendMessageToTongyiqianwen(groupId, userId, messageContent);
        String response = aiAnswer.getJSONObject("output").getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        //处理返回数据
        while (response.contains("\n")) {
            response = response.replace("\n", "");
        }
        while (response.contains("\"")) {
            response = response.replace("\"", "'");
        }
        //将用户以及AI回答存起来，作为下一次用户提问时的上下文数据
        List<AIRequestBody.Message.MessageContent> chatContextList = AppConstant.chatContext.getOrDefault(groupId + "-" + userId, new ArrayList<>());
        if (chatContextList.size() >= AppConstant.tongyiqianwenMaxContextCount) {
            //达到了上下文上限，清空最早的一组对话记录
            chatContextList.remove(0);
            chatContextList.remove(1);
        }
        AIRequestBody.Message.MessageContent userMessage = new AIRequestBody().new Message().new MessageContent();
        AIRequestBody.Message.MessageContent aiMessage = new AIRequestBody().new Message().new MessageContent();
        userMessage.setRole("user");
        userMessage.setContent(messageContent);
        aiMessage.setRole("assistant");
        aiMessage.setContent(response);
        chatContextList.add(userMessage);
        chatContextList.add(aiMessage);
        //将用户以及AI消息存起来，作为下一次用户提问时的上下文数据
        AppConstant.chatContext.put(groupId + "-" + userId, chatContextList);
        //将AI回答回复给用户
        RobotUtil.groupReply(groupId, messageId, response);
    }
}
