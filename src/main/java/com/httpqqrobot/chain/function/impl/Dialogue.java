package com.httpqqrobot.chain.function.impl;

import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.chain.function.common.Default;
import com.httpqqrobot.chain.function.common.Menu;
import com.httpqqrobot.chain.function.common.TodaySpeakRank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class Dialogue implements FunctionAct {

    @Resource
    private Menu menu;
    @Resource
    private TodaySpeakRank todaySpeakRank;
    @Resource
    private Default aDefault;

    @Override
    public void act(JSONObject json) {
        try {
//            String message = json.getString("message");
//            //不是艾特不回复
//            if (ObjectUtil.notEqual(message.split(" ")[0], "[CQ:at,qq=" + AppConstant.robotQQ + "]")) {
//                return;
//            }
//            switch (message.split(" ")[1]) {
//                case "菜单":
//                    menu.act(json, resp);
//                    break;
//                case "查询发言":
//                    todaySpeakRank.act(json, resp);
//                    break;
//                default:
//                    aDefault.act(json, resp);
//            }
        } catch (Exception e) {
            log.info("对话回复异常: {}", e.getMessage());
        }
    }
}
