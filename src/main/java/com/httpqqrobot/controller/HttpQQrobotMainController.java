package com.httpqqrobot.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.function.*;
import com.httpqqrobot.utils.AddUserMessage;
import com.httpqqrobot.utils.SendGetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@Slf4j
public class HttpQQrobotMainController {
    @Resource
    private StartOrStopFunction startOrStopFunction;
    @Resource
    private GroupRecall groupRecall;
    @Resource
    private Poke poke;
    @Resource
    private FlashImage flashImage;
    @Resource
    private Menu menu;
    @Resource
    private Default aDefault;
    @Resource
    private AddUserMessage addUserMessage;
    @Resource
    private TodaySpeakRank todaySpeakRank;

    @RateLimit(limit = 2)
    @RequestMapping("/handler")
    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String message;
            String key;
            String value;
            String qq;
            String nickName;
            String notice_type;
            String sub_type;
            String group_id;
            JSONObject json = SendGetMessage.getMessage(req);
            log.info(json.toJSONString());
            if (ObjectUtil.equal(AppConstant.GroupRecallStatus, AppConstant.TRUE)) {
                //发生消息撤回
                notice_type = json.getString("notice_type");
                if (ObjectUtil.equal(notice_type, "group_recall")) {
                    groupRecall.act(json);
                    return;
                }
            }
            if (ObjectUtil.equal(AppConstant.PokeStatus, AppConstant.TRUE)) {
                //机器人被"戳一戳"
                sub_type = json.getString("sub_type");
                if (ObjectUtil.equal(sub_type, "poke")) {
                    poke.act(json);
                    return;
                }
            }
            //获取消息信息
            group_id = json.getString("group_id");
            qq = json.getJSONObject("sender").getString("user_id");
            nickName = json.getJSONObject("sender").getString("nickname");
            message = json.getString("message");
            Date date = new Date(json.getLong("time") * 1000);
            //消息记录到数据库中
            addUserMessage.act(group_id, qq, nickName, message, date);
            if (ObjectUtil.equal(AppConstant.FlashImageStatus, AppConstant.TRUE)) {
                //有人发闪照
                if (message.contains("flash") && message.contains("CQ:image")) {
                    flashImage.act(message, resp);
                    return;
                }
            }
            //其他信息
            key = message.split(" ")[0];
            if (ObjectUtil.equal(key, "[CQ:at,qq=" + AppConstant.robotQQ + "]")) {//不是艾特不回复
                key = message.split(" ")[1] == null ? "无" : message.split(" ")[1];//获取操作
                switch (key) {
                    case "菜单":
                        menu.act(resp);
                        break;
                    case "开启":
                        //获取值
                        value = message.split(" ")[2];
                        startOrStopFunction.act(value, "1", qq, resp);
                        break;
                    case "关闭":
                        //获取值
                        value = message.split(" ")[2];
                        startOrStopFunction.act(value, "0", qq, resp);
                        break;
                    case "查询发言":
                        todaySpeakRank.act(group_id, qq, resp);
                        break;
                    default:
                        aDefault.act(qq, resp);
                }
            }
        } catch (Exception e) {
            log.info("handler异常: {}", e.getMessage());
        }
    }

    @RateLimit(limit = 2)
    @RequestMapping("/test")
    public void test(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("hello world");
    }

    //cron 定时属性  秒 分 时 日期 月份 星期几 年份
//    @Scheduled(cron = "* * * * * ?")
//    public void testScheduledTask() {
//
//    }
}
