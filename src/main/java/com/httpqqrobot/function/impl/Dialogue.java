package com.httpqqrobot.function.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.function.FunctionAct;
import com.httpqqrobot.function.common.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@ChainSequence(4)
public class Dialogue implements FunctionAct {

    @Resource
    private Menu menu;
    @Resource
    private StartFunction startFunction;
    @Resource
    private StopFunction stopFunction;
    @Resource
    private TodaySpeakRank todaySpeakRank;
    @Resource
    private Default aDefault;

    @Override
    public void act(JSONObject json, HttpServletResponse resp) {
        try {
            String message = json.getString("message");
            //不是艾特不回复
            if (ObjectUtil.notEqual(message.split(" ")[0], "[CQ:at,qq=" + AppConstant.robotQQ + "]")) {
                return;
            }
            switch (message.split(" ")[1]) {
                case "菜单":
                    menu.act(json, resp);
                    break;
                case "开启":
                    startFunction.act(json, resp);
                    break;
                case "关闭":
                    stopFunction.act(json, resp);
                    break;
                case "查询发言":
                    todaySpeakRank.act(json, resp);
                    break;
                default:
                    aDefault.act(json, resp);
            }
        } catch (Exception e) {
            log.info("对话回复异常: {}", e.getMessage());
        }
    }
}
