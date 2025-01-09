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
            //不是艾特不回复
            if (ObjectUtil.notEqual(message.split(" ")[0], "[CQ:at,qq=" + AppConstant.robotQQ + "]")) {
                return;
            }
            String groupId = json.getString("group_id");
            String messageContent = message.split(" ")[1];
            String messageId = json.getString("message_id");
            String userId = json.getString("user_id");
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
            AIRequestBody.Message.MessageContent userMessage = new AIRequestBody().new Message().new MessageContent();
            AIRequestBody.Message.MessageContent aiMessage = new AIRequestBody().new Message().new MessageContent();
            userMessage.setRole("user");
            userMessage.setContent(messageContent);
            aiMessage.setRole("assistant");
            aiMessage.setContent(response);
            chatContextList.add(userMessage);
            chatContextList.add(aiMessage);
            if (chatContextList.size() >= AppConstant.tongyiqianwenMaxContextCount) {
                //达到了上下文上限，清空对话记录
                chatContextList.clear();
            }
            AppConstant.chatContext.put(groupId + "-" + userId, chatContextList);
            //将AI回答回复给用户
            RobotUtil.groupReply(groupId, messageId, response);
        } catch (Exception e) {
            log.info("对话回复异常: {}", e.getMessage());
        }
    }
}
