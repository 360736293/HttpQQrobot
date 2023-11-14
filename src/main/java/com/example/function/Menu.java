package com.example.function;

import com.alibaba.fastjson.JSONObject;
import com.example.constant.AppConstant;
import com.example.utils.SendGetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class Menu {

    public void act(HttpServletResponse resp) {
        try {
            JSONObject answer = new JSONObject();
            answer.put("reply", "" +
                    "菜单(@机器人发送)\n" +
                    "[CQ:face,id=89](" + (AppConstant.GroupRecallStatus.equals("1") ? "on" : "off") + ")防撤回\n" +
                    "有人撤回时触发\n" +
                    "[CQ:face,id=89](" + (AppConstant.FlashImageStatus.equals("1") ? "on" : "off") + ")防闪照\n" +
                    "有人发闪照时触发\n" +
                    "[CQ:face,id=89](" + (AppConstant.PokeStatus.equals("1") ? "on" : "off") + ")戳一戳\n" +
                    "对我使用\"戳一戳\"\n" +
                    "[CQ:face,id=89](" + (AppConstant.TodaySpeakRank.equals("1") ? "on" : "off") + ")查询群内今日发言排名\n" +
                    "@机器人 查询发言\n");
            SendGetMessage.sendMessage(answer, resp);
        } catch (IOException e) {
            log.info("菜单回复异常: {}", e.getMessage());
        }
    }
}
