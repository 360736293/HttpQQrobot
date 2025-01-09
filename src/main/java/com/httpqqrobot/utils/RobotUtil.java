package com.httpqqrobot.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RobotUtil {
    /**
     * 机器人主动发起消息
     *
     * @param subUrl 主动发起消息请求路径
     * @param body   发送内容
     * @return
     */
    public static JSONObject sendMessage(String subUrl, String body) {
        JSONObject response = JSONObject.parseObject(
                HttpRequest
                        .post(AppConstant.robotIp + subUrl)
                        .header("Content-Type", "application/json")
                        .body(body)
                        .execute()
                        .body()
        );
        log.info("response information: {}", response.toJSONString());
        return response;
    }

    /**
     * 机器人回复群消息
     *
     * @param groupId      待回复群ID
     * @param messageId    待回复消息ID
     * @param replyContent 回复内容
     * @return
     */
    public static JSONObject groupReply(String groupId, String messageId, String replyContent) {
        JSONObject response = JSONObject.parseObject(
                HttpRequest
                        .post(AppConstant.robotIp + "/send_group_msg")
                        .header("Content-Type", "application/json")
                        .body("{\n" +
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
                                "                \"text\": \"" + replyContent + "\"\n" +
                                "            }\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")
                        .execute()
                        .body()
        );
        log.info("response information: {}", response.toJSONString());
        return response;
    }

    /**
     * 机器人发送消息到通义千问
     *
     * @param body 消息内容
     * @return
     */
    public static JSONObject sendMessageToTongyiqianwen(String body) {
        JSONObject response = JSONObject.parseObject(
                HttpRequest
                        .post("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
                        .header("Authorization", "Bearer " + AppConstant.tongyiqianwenApiKey)
                        .header("Content-Type", "application/json; utf-8")
                        .header("Accept", "application/json")
                        .body("{\n" +
                                "    \"model\": \"" + AppConstant.tongyiqianwenModel + "\",\n" +
                                "    \"input\":{\n" +
                                "        \"messages\":[      \n" +
                                "            {\n" +
                                "                \"role\": \"system\",\n" +
                                "                \"content\": \"" + AppConstant.promptWords + "\"\n" +
                                "            },\n" +
                                "            {\n" +
                                "                \"role\": \"user\",\n" +
                                "                \"content\": \"" + body + "\"\n" +
                                "            }\n" +
                                "        ]\n" +
                                "    },\n" +
                                "    \"parameters\": {\n" +
                                "        \"result_format\": \"message\"\n" +
                                "    }\n" +
                                "}")
                        .execute()
                        .body()
        );
        log.info("response information: {}", response.toJSONString());
        return response;
    }
}
