package com.httpqqrobot.chain.function.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.httpqqrobot.annotation.Authorize;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.function.common.CommonMethod;
import com.httpqqrobot.chain.function.FunctionHandler;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.exception.AuthorizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@ChainSequence(1)
public class Dialogue implements FunctionHandler {

    @Resource
    public CommonMethod commonMethod;

    @Authorize(roleValue = 1)
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
                    commonMethod.clearMemory(groupId, messageId, userId);
                    break;
                case "群消息总结":
                    commonMethod.groupMessageSummary(groupId, messageId, userId, messageSplit[2]);
                    break;
                case "AI联网":
                    commonMethod.aiTalk(groupId, messageId, userId, commonMethod.spliceContent(messageSplit, true), true);
                    break;
                case "Steam打折消息订阅":
                    commonMethod.subscribeStreamDiscountNotify(groupId, messageId, userId, messageSplit[2], AppConstant.INSERT);
                    break;
                case "Steam打折消息订阅查询":
                    commonMethod.subscribeStreamDiscountNotify(groupId, messageId, userId, null, AppConstant.QUERY);
                    break;
                case "Steam打折消息订阅删除":
                    commonMethod.subscribeStreamDiscountNotify(groupId, messageId, userId, messageSplit[2], AppConstant.DELETE);
                    break;
                case "菜单":
                    commonMethod.showMenu(groupId, messageId);
                    break;
                default:
                    commonMethod.aiTalk(groupId, messageId, userId, commonMethod.spliceContent(messageSplit, false), false);
            }
        } catch (AuthorizeException e) {
            throw new AuthorizeException();
        } catch (Throwable e) {
            log.error("对话回复异常: ", e);
        }
    }

}
