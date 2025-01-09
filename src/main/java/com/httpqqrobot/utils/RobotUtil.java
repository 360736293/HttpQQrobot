package com.httpqqrobot.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.AIRequestBody;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

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
                        .body("{" +
                                "    \"group_id\": " + groupId + "," +
                                "    \"message\": [" +
                                "        {" +
                                "            \"type\": \"reply\"," +
                                "            \"data\": {" +
                                "                \"id\": " + messageId + "" +
                                "            }" +
                                "        }," +
                                "        {" +
                                "            \"type\": \"text\"," +
                                "            \"data\": {" +
                                "                \"text\": \"" + replyContent + "\"" +
                                "            }" +
                                "        }" +
                                "    ]" +
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
     * @param content 消息内容
     * @return
     */
    public static JSONObject sendMessageToTongyiqianwen(String groupId, String userId, String content) {
//        {
//            "model": "",
//            "input":{
//                "messages":[
//                    {
//                        "role": "system",
//                        "content": ""
//                    },
//                    {
//                        "role": "user",
//                        "content": ""
//                    },
//                    {
//                        "role": "assistant",
//                        "content": ""
//                    }
//                ]
//            },
//            "parameters": {
//                "result_format": "message"
//            }
//        }
        AIRequestBody body = new AIRequestBody();
        body.setModel(AppConstant.tongyiqianwenModel);
        AIRequestBody.Message input = body.new Message();
        List<AIRequestBody.Message.MessageContent> messages = new ArrayList<>();
        input.setMessages(messages);
        body.setInput(input);
        AIRequestBody.Message.MessageContent systemMessageContent = input.new MessageContent();
        systemMessageContent.setRole("system");
        systemMessageContent.setContent(AppConstant.promptWords);
        input.getMessages().add(systemMessageContent);

        //按照用户和AI的历史对话记录依次填充消息内容
        List<AIRequestBody.Message.MessageContent> messageContents = AppConstant.chatContext.get(groupId + "-" + userId);
        if (ObjectUtil.isNotEmpty(messageContents)) {
            for (AIRequestBody.Message.MessageContent messageContent : messageContents) {
                input.getMessages().add(messageContent);
            }
        }
        //填充用户最新的消息内容
        AIRequestBody.Message.MessageContent messageContent = input.new MessageContent();
        messageContent.setRole("user");
        messageContent.setContent(content);
        input.getMessages().add(messageContent);

        AIRequestBody.ResultFormat resultFormat = body.new ResultFormat();
        resultFormat.setResult_format("message");
        body.setParameters(resultFormat);
        JSONObject response = JSONObject.parseObject(
                HttpRequest
                        .post("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
                        .header("Authorization", "Bearer " + AppConstant.tongyiqianwenApiKey)
                        .header("Content-Type", "application/json; utf-8")
                        .header("Accept", "application/json")
                        .body(JSONObject.toJSONString(body))
                        .execute()
                        .body()
        );
        log.info("response information: {}", response.toJSONString());
        return response;
    }
}
