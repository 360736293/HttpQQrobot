package com.httpqqrobot.chain.function.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.utils.RobotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
            JSONObject aiAnswer = RobotUtil.sendMessageToTongyiqianwen(messageContent);
            String response = aiAnswer.getJSONObject("output").getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            //处理返回数据
            while (response.contains("\n")) {
                response = response.replace("\n", "");
            }
            while (response.contains("\\")) {
                response = response.replace("\\", "");
            }
            RobotUtil.groupReply(groupId, messageId, response);
        } catch (Exception e) {
            log.info("对话回复异常: {}", e.getMessage());
        }
    }
}
