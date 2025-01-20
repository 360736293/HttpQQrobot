package com.httpqqrobot.chain.function.common;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.AIRequestBody;
import com.httpqqrobot.entity.RobotGroupIntegrativeReplyRequestBody;
import com.httpqqrobot.entity.SteamDiscountNotify;
import com.httpqqrobot.entity.UserAuthority;
import com.httpqqrobot.entity.UserMessage;
import com.httpqqrobot.entity.UserRoleEnum;
import com.httpqqrobot.service.ISteamDiscountNotifyService;
import com.httpqqrobot.service.IUserAuthorityService;
import com.httpqqrobot.service.IUserMessageService;
import com.httpqqrobot.utils.RobotUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CommonMethod {

    @Resource
    public IUserMessageService userMessageServiceImpl;

    @Resource
    private ISteamDiscountNotifyService steamDiscountNotifyServiceImpl;

    @Resource
    private IUserAuthorityService userAuthorityService;

    public void showMenu(String groupId, String messageId) {
        StringBuilder menu = new StringBuilder();
        menu.append("所有与机器人的互动都是通过@机器人，通过对话来触发的。").append("\n");
        menu.append("\n");
        menu.append("示例：[任意一句话]").append("\n");
        menu.append("描述：与机器人的对话支持记忆（联想上下文），每个群每个人的记忆空间都是独立的，机器人最多能记住最近的50条对话记录（用户和机器人各25条，新的对话记录会替换最早的对话记录）。").append("\n");
        menu.append("\n");
        menu.append("示例：清除记忆").append("\n");
        menu.append("描述：清除机器人的记忆，也就是清除上下文联想，适用于开启一个新的话题。").append("\n");
        menu.append("\n");
        menu.append("示例：群消息总结 2222-02-01").append("\n");
        menu.append("描述：机器人总结2222-02-01这天当前群消息内容。").append("\n");
        menu.append("\n");
        menu.append("示例：AI联网 [任意一句话]").append("\n");
        menu.append("描述：与机器人的对话会先经过机器人联网搜索查阅资料之后才会回复，这有效解决了机器人已读乱回的情况以及资料库老旧的问题。").append("\n");
        menu.append("\n");
        menu.append("示例：Steam打折消息订阅 [Steam商店地址]").append("\n");
        menu.append("描述：订阅指定商店地址游戏的打折通知。").append("\n");
        menu.append("\n");
        menu.append("示例：Steam打折消息订阅查询").append("\n");
        menu.append("描述：查询当前已经订阅的全部记录。").append("\n");
        menu.append("\n");
        menu.append("示例：Steam打折消息订阅删除 [Steam商店地址]").append("\n");
        menu.append("描述：删除指定商店地址游戏的打折通知。").append("\n");
        menu.append("\n");
        menu.append("示例：ban [QQ号]").append("\n");
        menu.append("描述：需要管理员以上的权限，指定QQ号会被禁止，无法与机器人互动。").append("\n");
        menu.append("\n");
        menu.append("示例：unban [QQ号]").append("\n");
        menu.append("描述：需要管理员以上的权限，指定QQ号恢复为游客权限。").append("\n");
        RobotUtil.groupReply(groupId, messageId, menu.toString());
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
                HttpRequest httpRequest = HttpRequest
                        .get(url)
                        .header("Cookie", "wants_mature_content=1; birthtime=915120001; lastagecheckage=1-January-1999;");
                if (ObjectUtil.isNotEmpty(AppConstant.proxyIP) && ObjectUtil.isNotEmpty(AppConstant.proxyPort)) {
                    httpRequest.setHttpProxy(AppConstant.proxyIP, AppConstant.proxyPort);
                }
                String response = httpRequest.execute().body();
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
                aNew.setText("共 " + gameList.size() + " 条");
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
        List<AIRequestBody.Message> chatContextList = AppConstant.chatContext.get(groupId + "-" + userId);
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
        List<AIRequestBody.Message> chatContextList = AppConstant.chatContext.getOrDefault(groupId + "-" + userId, new ArrayList<>());
        if (chatContextList.size() >= AppConstant.tongyiqianwenMaxContextCount) {
            //达到了上下文上限，清空最早的一组对话记录
            chatContextList.remove(0);
            chatContextList.remove(1);
        }
        AIRequestBody.Message userMessage = new AIRequestBody().new Message();
        AIRequestBody.Message aiMessage = new AIRequestBody().new Message();
        userMessage.setRole("user");
        userMessage.setContent(userMessageContent);
        aiMessage.setRole("assistant");
        aiMessage.setContent(robotMessageContent);
        chatContextList.add(userMessage);
        chatContextList.add(aiMessage);
        AppConstant.chatContext.put(groupId + "-" + userId, chatContextList);
    }

    @Authorize(roleValue = 7)
    public void ban(String groupId, String messageId, String userId, String targetUserId) {
        try {
            Integer userRoleValue = AppConstant.userAuthorityMap.get(userId);
            //目标用户可能是游客
            Integer targetUserRoleValue = AppConstant.userAuthorityMap.getOrDefault(targetUserId, UserRoleEnum.Guest.getRoleValue());
            //超级管理员不能被ban
            if (targetUserRoleValue == UserRoleEnum.SuperAdmin.getRoleValue()) {
                RobotUtil.groupReply(groupId, messageId, "管理员不可以互相ban");
                return;
            }
            //除非超级管理员，管理员不能互相ban
            if (userRoleValue != UserRoleEnum.SuperAdmin.getRoleValue() && targetUserRoleValue == UserRoleEnum.Admin.getRoleValue()) {
                RobotUtil.groupReply(groupId, messageId, "管理员不可以互相ban");
                return;
            }
            //查询该用户是否存在
            UserAuthority userAuthority = userAuthorityService.lambdaQuery().eq(UserAuthority::getUserId, targetUserId).one();
            if (ObjectUtil.isEmpty(userAuthority)) {
                //ban用户
                UserAuthority newUserAuthority = new UserAuthority();
                newUserAuthority.setId(IdUtil.getSnowflakeNextIdStr());
                newUserAuthority.setUserId(targetUserId);
                newUserAuthority.setRoleName(UserRoleEnum.Banned.getRoleName());
                newUserAuthority.setRoleValue(UserRoleEnum.Banned.getRoleValue());
                userAuthorityService.save(newUserAuthority);
                RobotUtil.groupReply(groupId, messageId, "该用户已被ban");
                return;
            }
            if (userAuthority.getRoleValue() == UserRoleEnum.Banned.getRoleValue()) {
                RobotUtil.groupReply(groupId, messageId, "该用户已被ban");
                return;
            }
            userAuthority.setRoleName(UserRoleEnum.Banned.getRoleName());
            userAuthority.setRoleValue(UserRoleEnum.Banned.getRoleValue());
            userAuthorityService.lambdaUpdate().eq(UserAuthority::getUserId, targetUserId).update(userAuthority);
            RobotUtil.groupReply(groupId, messageId, "该用户已被ban");
            //更新缓存的用户权限数据
            AppConstant.userAuthorityMap.put(targetUserId, UserRoleEnum.Banned.getRoleValue());
        } catch (Throwable e) {
            log.error("ban用户异常: ", e);
        }
    }

    @Authorize(roleValue = 7)
    public void unban(String groupId, String messageId, String userId, String targetUserId) {
        try {
            //直接删除指定用户的权限记录
            userAuthorityService.lambdaUpdate().eq(UserAuthority::getUserId, targetUserId).remove();
            RobotUtil.groupReply(groupId, messageId, "该用户已被unban");
            //更新缓存的用户权限数据
            AppConstant.userAuthorityMap.put(targetUserId, UserRoleEnum.Guest.getRoleValue());
        } catch (Throwable e) {
            log.error("unban用户异常: ", e);
        }
    }
}
