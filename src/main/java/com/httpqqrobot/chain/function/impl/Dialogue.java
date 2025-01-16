package com.httpqqrobot.chain.function.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.AIRequestBody;
import com.httpqqrobot.entity.RobotGroupIntegrativeReplyRequestBody;
import com.httpqqrobot.entity.SteamDiscountNotify;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.service.ISteamDiscountNotifyService;
import com.httpqqrobot.service.IUserMessageService;
import com.httpqqrobot.utils.RobotUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@ChainSequence(1)
public class Dialogue implements FunctionAct {

    @Resource
    public IUserMessageService userMessageServiceImpl;

    @Resource
    private ISteamDiscountNotifyService steamDiscountNotifyServiceImpl;

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
                case "群消息总结":
                    groupMessageSummary(groupId, messageId, userId, messageSplit[2]);
                    break;
                case "AI联网":
                    aiTalk(groupId, messageId, userId, spliceContent(messageSplit, true), true);
                    break;
                case "Steam打折消息订阅":
                    subscribeStreamDiscountNotify(groupId, messageId, userId, messageSplit[2], AppConstant.INSERT);
                    break;
                case "Steam打折消息订阅查询":
                    subscribeStreamDiscountNotify(groupId, messageId, userId, null, AppConstant.QUERY);
                    break;
                case "Steam打折消息订阅删除":
                    subscribeStreamDiscountNotify(groupId, messageId, userId, messageSplit[2], AppConstant.DELETE);
                    break;
                case "Steam打折消息预测":

                    break;
                default:
                    aiTalk(groupId, messageId, userId, spliceContent(messageSplit, false), false);
            }
        } catch (Exception e) {
            log.info("对话回复异常: ", e);
        }
    }


    public void subscribeStreamDiscountNotify(String groupId, String messageId, String userId, String url, String operation) {
        String gameId = null;
        if (!StringUtils.equals(operation, AppConstant.QUERY)) {
            //验证输入的url是否合法
            if (StringUtils.isEmpty(url)) {
                RobotUtil.groupReply(groupId, messageId, "Steam网址路径不合法");
                return;
            }
            String regexp = "https{0,1}://store.steampowered.com/app/(\\d{1,})/.*";
            String res = ReUtil.get(regexp, url, 0);
            if (!StringUtils.equals(res, url)) {
                RobotUtil.groupReply(groupId, messageId, "Steam网址路径不合法");
                return;
            }
            gameId = ReUtil.get(regexp, url, 1);
        }
        switch (operation) {
            case AppConstant.INSERT:
                //在订阅定时任务表中查询该游戏
                SteamDiscountNotify insertTarget = steamDiscountNotifyServiceImpl.lambdaQuery().eq(SteamDiscountNotify::getGameId, gameId).one();
                if (ObjectUtil.isNotEmpty(insertTarget)) {
                    RobotUtil.groupReply(groupId, messageId, "请勿重复订阅");
                    return;
                }
                //查询该网站获取游戏名以及封面图网址
                String response = HttpRequest
                        .get(url)
                        .header("Cookie", "wants_mature_content=1; birthtime=915120001; lastagecheckage=1-January-1999;")
                        .setHttpProxy(AppConstant.proxyIP, AppConstant.proxyPort)
                        .execute()
                        .body();
                String imgUrl = ReUtil.get("<img class=\"game_header_image_full\" src=\"(.*?)\">", response, 1);
                String gameName = ReUtil.get("<div id=\"appHubAppName_responsive\" style=\"display: none;\" class=\"apphub_AppName\">(.*?)</div>", response, 1);
                SteamDiscountNotify steamDiscountNotify = new SteamDiscountNotify();
                steamDiscountNotify.setId(IdUtil.getSnowflakeNextIdStr());
                steamDiscountNotify.setGameId(gameId);
                steamDiscountNotify.setUserId(userId);
                steamDiscountNotify.setUrl(url);
                steamDiscountNotify.setGameName(gameName);
                steamDiscountNotify.setImageUrl(imgUrl);
                steamDiscountNotifyServiceImpl.save(steamDiscountNotify);
                RobotUtil.groupReply(groupId, messageId, "Steam打折消息订阅成功");
                break;
            case AppConstant.QUERY:
                //查询出该用户所有的订阅信息
                List<SteamDiscountNotify> gameList = steamDiscountNotifyServiceImpl.lambdaQuery().eq(SteamDiscountNotify::getUserId, userId).list();
                if (ObjectUtil.isEmpty(gameList)) {
                    RobotUtil.groupReply(groupId, messageId, "无订阅记录");
                    return;
                }
                //封装请求实体基础信息
                RobotGroupIntegrativeReplyRequestBody robotGroupIntegrativeReplyRequestBody = new RobotGroupIntegrativeReplyRequestBody();
                robotGroupIntegrativeReplyRequestBody.setGroup_id(groupId);
                robotGroupIntegrativeReplyRequestBody.setPrompt("Steam打折消息订阅记录");
                robotGroupIntegrativeReplyRequestBody.setSummary("QQ：" + gameList.get(0).getUserId());
                robotGroupIntegrativeReplyRequestBody.setSource("Steam打折消息订阅记录");
                List<RobotGroupIntegrativeReplyRequestBody.New> news = new ArrayList<>();
                RobotGroupIntegrativeReplyRequestBody.New aNew = robotGroupIntegrativeReplyRequestBody.new New();
                aNew.setText("Steam打折消息订阅记录");
                news.add(aNew);
                robotGroupIntegrativeReplyRequestBody.setNews(news);
                List<RobotGroupIntegrativeReplyRequestBody.Message> messages = new ArrayList<>();
                for (SteamDiscountNotify game : gameList) {
                    //挨个遍历查找每一个游戏对应的图片和游戏名，然后封装到请求实体类里
                    RobotGroupIntegrativeReplyRequestBody.Message message = robotGroupIntegrativeReplyRequestBody.new Message();
                    message.setType("node");
                    RobotGroupIntegrativeReplyRequestBody.Message.OuterData outerData = message.new OuterData();
                    outerData.setUser_id(AppConstant.robotQQ);
                    outerData.setNickname(AppConstant.robotQQ);
                    List<RobotGroupIntegrativeReplyRequestBody.Message.OuterData.Content> contents = new ArrayList<>();
                    RobotGroupIntegrativeReplyRequestBody.Message.OuterData.Content content = outerData.new Content();
                    content.setType("text");
                    RobotGroupIntegrativeReplyRequestBody.Message.OuterData.Content.InnerData innerData = content.new InnerData();
                    innerData.setText("游戏名：" + game.getGameName() + "\n商店地址：" + game.getUrl());
                    content.setData(innerData);
                    contents.add(content);
                    content = outerData.new Content();
                    content.setType("image");
                    innerData = content.new InnerData();
                    innerData.setFile(game.getImageUrl());
                    content.setData(innerData);
                    contents.add(content);
                    outerData.setContent(contents);
                    message.setData(outerData);
                    messages.add(message);
                }
                robotGroupIntegrativeReplyRequestBody.setMessages(messages);
                RobotUtil.groupIntegrativeReply(robotGroupIntegrativeReplyRequestBody);
                break;
            case AppConstant.DELETE:
                //在订阅定时任务表中查询该游戏
                SteamDiscountNotify deleteTarget = steamDiscountNotifyServiceImpl.lambdaQuery().eq(SteamDiscountNotify::getGameId, gameId).one();
                if (ObjectUtil.isEmpty(deleteTarget)) {
                    RobotUtil.groupReply(groupId, messageId, "未订阅该游戏");
                    return;
                }
                steamDiscountNotifyServiceImpl.lambdaUpdate().eq(SteamDiscountNotify::getGameId, gameId).remove();
                RobotUtil.groupReply(groupId, messageId, "Steam打折消息订阅删除成功");
                break;
            default:
                break;
        }
    }

    public void groupMessageSummary(String groupId, String messageId, String userId, String date) {
        //传入的日期格式形如2025-01-13，跟查询出该群组该日所有消息记录
        List<UserMessage> messageList = userMessageServiceImpl.lambdaQuery().eq(UserMessage::getGroupId, groupId).between(UserMessage::getTime, date + " 00:00:00", date + " 23:59:59").eq(UserMessage::getPostType, "message").select(UserMessage::getMessage).list();
        //将查询出来的聊天记录消息数据清除QQ表情，图片，艾特等内置消息，并且拼接上序号，以及分割符号
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++) {
            String tempMessage = messageList.get(i).getMessage();
            tempMessage = ReUtil.delAll("\\[CQ.*?\\] *", tempMessage);
            if (ObjectUtil.isEmpty(tempMessage)) {
                continue;
            }
            message.append(i + 1).append("、 ").append(tempMessage).append("\n");
        }
        //将消息发送给AI进行总结
        String response = RobotUtil.sendMessageToTongyiqianwen(groupId, null, message.toString(), false, true);
        //将用户以及AI消息存起来，作为下一次用户提问时的上下文数据
        saveResponseToContext(groupId, userId, "群消息总结 " + date, response);
        //将总结内容发送到QQ群
        RobotUtil.groupReply(groupId, messageId, response);
    }

    public String spliceContent(String[] messageSplit, boolean containCommand) {
        //因为内容中间可能也会存在空格而导致被分割，所以需要拼接起来
        StringBuilder message = new StringBuilder();
        if (containCommand) {
            //请求内容存在指令关键字，所以要跳过
            for (int i = 2; i < messageSplit.length; i++) {
                message.append(messageSplit[i]).append(" ");
            }
        } else {
            for (int i = 1; i < messageSplit.length; i++) {
                message.append(messageSplit[i]).append(" ");
            }
        }
        return message.toString();
    }

    public void clearMemory(String groupId, String messageId, String userId) {
        List<AIRequestBody.Message.MessageContent> chatContextList = AppConstant.chatContext.get(groupId + "-" + userId);
        if (ObjectUtil.isNotEmpty(chatContextList)) {
            chatContextList.clear();
            AppConstant.chatContext.put(groupId + "-" + userId, chatContextList);
        }
        RobotUtil.groupReply(groupId, messageId, "记忆已清除");
    }

    public void aiTalk(String groupId, String messageId, String userId, String messageContent, boolean withNet) {
        String response = RobotUtil.sendMessageToTongyiqianwen(groupId, userId, messageContent, withNet, false);
        //将用户以及AI消息存起来，作为下一次用户提问时的上下文数据
        saveResponseToContext(groupId, userId, messageContent, response);
        //将AI回答回复给用户
        RobotUtil.groupReply(groupId, messageId, response);
    }

    public void saveResponseToContext(String groupId, String userId, String userMessageContent, String robotMessageContent) {
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
        userMessage.setContent(userMessageContent);
        aiMessage.setRole("assistant");
        aiMessage.setContent(robotMessageContent);
        chatContextList.add(userMessage);
        chatContextList.add(aiMessage);
        AppConstant.chatContext.put(groupId + "-" + userId, chatContextList);
    }
}
