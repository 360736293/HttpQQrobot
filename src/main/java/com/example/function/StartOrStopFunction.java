package com.example.function;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.FunctionStatus;
import com.example.service.IFunctionStatusService;
import com.example.utils.LoadConfig;
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
    private LoadConfig loadConfig;

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
                case "查询群内今日发言排名":
                    message = "TodaySpeakRank";
                    break;
            }
            boolean flag = functionStatusService.update(
                    Wrappers.lambdaUpdate(FunctionStatus.class)
                            .set(FunctionStatus::getStatus, status)
                            .eq(FunctionStatus::getName, message)
            );
            if (flag) {
                answer.put("reply", "[CQ:at,qq=" + qq + "]操作成功!");
                loadConfig.act();
            } else {
                answer.put("reply", "[CQ:at,qq=" + qq + "]操作失败!");
            }
            SendGetMessage.sendMessage(answer, resp);
        } catch (Exception e) {
            log.info("开启关闭功能异常: {}", e.getMessage());
        }
    }
}
