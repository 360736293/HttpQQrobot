package com.httpqqrobot.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.AIRequestBody;
import com.httpqqrobot.entity.RobotResponseBody;
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
     * //        {
     * //            "group_id": "",
     * //            "message": [
     * //                {
     * //                    "type": "reply",
     * //                    "data": {
     * //                        "id": ""
     * //                    }
     * //                },
     * //                {
     * //                    "type": "text",
     * //                    "data": {
     * //                        "text": ""
     * //                    }
     * //                }
     * //            ]
     * //        }
     *
     * @param groupId      待回复群ID
     * @param messageId    待回复消息ID
     * @param replyContent 回复内容
     * @return
     */
    public static JSONObject groupReply(String groupId, String messageId, String replyContent) {
        RobotResponseBody robotResponseBody = new RobotResponseBody();
        robotResponseBody.setGroup_id(groupId);
        List<RobotResponseBody.Message> messages = new ArrayList<>();
        RobotResponseBody.Message message = robotResponseBody.new Message();
        message.setType("reply");
        RobotResponseBody.Message.SubMessage subMessage = message.new SubMessage();
        subMessage.setId(messageId);
        message.setData(subMessage);
        messages.add(message);
        message = robotResponseBody.new Message();
        message.setType("text");
        subMessage = message.new SubMessage();
        subMessage.setText(replyContent);
        message.setData(subMessage);
        messages.add(message);
        robotResponseBody.setMessage(messages);
        JSONObject response = JSONObject.parseObject(
                HttpRequest
                        .post(AppConstant.robotIp + "/send_group_msg")
                        .header("Content-Type", "application/json")
                        .body(JSONObject.toJSONString(robotResponseBody))
                        .execute()
                        .body()
        );
        log.info("response information: {}", response.toJSONString());
        return response;
    }

    /**
     * 机器人发送消息到通义千问
     * //        {
     * //            "model": "",
     * //            "input":{
     * //                "messages":[
     * //                    {
     * //                        "role": "system",
     * //                        "content": ""
     * //                    },
     * //                    {
     * //                        "role": "user",
     * //                        "content": ""
     * //                    },
     * //                    {
     * //                        "role": "assistant",
     * //                        "content": ""
     * //                    }
     * //                ]
     * //            },
     * //            "parameters": {
     * //                "result_format": "message"
     * //            }
     * //        }
     *
     * @param groupId   待回复群ID
     * @param userId    待回复用户ID
     * @param content   用户消息内容
     * @param withNet   是否联网
     * @param isSummary 是否是总结请求
     * @return
     */
    public static String sendMessageToTongyiqianwen(String groupId, String userId, String content, boolean withNet, boolean isSummary) {
        //TODO 使用策略模式重构这里，顺便可以兼容多种AI大模型
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

        if (isSummary) {
            AIRequestBody.Message.MessageContent messageContent = input.new MessageContent();
            messageContent.setRole("user");
            messageContent.setContent("我接下来会给你一些文字记录，这些文字记录是一天内产生的消息记录，每条记录都会以数字序号作为起始，换行符作为结束。你将我发给你的记录进行总结，整理出重点。注意，在后续的回复中不要提到本条消息的任何内容。");
            input.getMessages().add(messageContent);
            messageContent = input.new MessageContent();
            messageContent.setRole("user");
            messageContent.setContent(content);
            input.getMessages().add(messageContent);
        } else {
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
        }

        AIRequestBody.ResultFormat resultFormat = body.new ResultFormat();
        resultFormat.setResult_format("message");
        body.setParameters(resultFormat);
        JSONObject aiAnswer = JSONObject.parseObject(
                HttpRequest
                        .post("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
                        .header("Authorization", "Bearer " + AppConstant.tongyiqianwenApiKey)
                        .header("Content-Type", "application/json; utf-8")
                        .header("Accept", "application/json")
                        .body(JSONObject.toJSONString(body))
                        .execute()
                        .body()
        );
        log.info("response information: {}", aiAnswer.toJSONString());
        return aiAnswer.getJSONObject("output").getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}
