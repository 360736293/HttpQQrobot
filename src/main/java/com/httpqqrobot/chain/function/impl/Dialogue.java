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
            RobotUtil.sendMessage("/send_group_msg",
                    "{\n" +
                            "    \"group_id\": " + groupId + ",\n" +
                            "    \"message\": [\n" +
                            "        {\n" +
                            "            \"type\": \"reply\",\n" +
                            "            \"data\": {\n" +
                            "                \"id\": " + messageId + "\n" +
                            "            }\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"type\": \"text\",\n" +
                            "            \"data\": {\n" +
                            "                \"text\": \"" + messageContent + "\"\n" +
                            "            }\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}"
            );
        } catch (Exception e) {
            log.info("对话回复异常: {}", e.getMessage());
        }
    }
}
