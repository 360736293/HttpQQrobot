package com.example.function;

import com.alibaba.fastjson.JSONObject;
import com.example.service.IFunctionStatusService;
import com.example.utils.FunctionStatusLoad;
import com.example.utils.SendGetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class StartOrStopFunction {
    @Resource
    private IFunctionStatusService functionStatusService;
    @Resource
    private FunctionStatusLoad functionStatusLoad;

    public void act(String message, String status, String qq, HttpServletResponse resp) {
        try {
            JSONObject answer = new JSONObject();
            switch (message) {
                case "防撤回":
                    message = "GroupRecallStatus";
                    break;
                case "防闪照":
                    message = "FlashImageStatus";
                    break;
                case "戳一戳":
                    message = "PokeStatus";
                    break;
                case "达芬奇Ⅲ型聊天机器人":
                    message = "DavinciChatBotStatus";
                    break;
                case "查询群内今日发言排名":
                    message = "TodaySpeakRank";
                    break;
                case "查询当前服务器信息":
                    message = "ServerInfoStatus";
                    break;
            }
            Integer flag = functionStatusService.changeFunctionStatus(message, status);
            if (flag == 1) {
                answer.put("reply", "[CQ:at,qq=" + qq + "]操作成功!");
                functionStatusLoad.act();
            } else {
                answer.put("reply", "[CQ:at,qq=" + qq + "]操作失败!");
            }
            SendGetMessage.sendMessage(answer, resp);
        } catch (Exception e) {
            log.info("开启关闭功能异常: {}", e.getMessage());
        }
    }
}
