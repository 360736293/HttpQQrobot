package com.httpqqrobot.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
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
     * @param groupId 待回复群ID
     * @param userId  待回复用户ID
     * @param content 用户消息内容
     * @param withNet 是否联网
     * @return
     */
    public static JSONObject sendMessageToTongyiqianwen(String groupId, String userId, String content, boolean withNet) {
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
        if (withNet) {
            //联网查询用户消息内容，整理后一并传给GPT
            AIRequestBody.Message.MessageContent messageContent = input.new MessageContent();
            messageContent.setRole("user");
            messageContent.setContent("我接下来会给你一些资料，每条资料都会有一个对应的数字序号。你以我提供的资料为参考信息回复我的问题。注意，在后续的回复中不要提到本条消息的任何内容。");
            input.getMessages().add(messageContent);
            List<String> searchResults = GoogleUtil.search(content);
            int i = 1;
            for (String searchResult : searchResults) {
                messageContent = input.new MessageContent();
                messageContent.setRole("user");
                messageContent.setContent(i + "、" + searchResult);
                input.getMessages().add(messageContent);
                i++;
            }
            messageContent = input.new MessageContent();
            messageContent.setRole("user");
            messageContent.setContent("问题：" + content);
            input.getMessages().add(messageContent);
        } else {
            //不联网，直接将问题内容传给GPT
            AIRequestBody.Message.MessageContent messageContent = input.new MessageContent();
            messageContent.setRole("user");
            messageContent.setContent(content);
            input.getMessages().add(messageContent);
        }

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
