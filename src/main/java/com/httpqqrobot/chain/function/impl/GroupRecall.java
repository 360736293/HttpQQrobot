package com.httpqqrobot.chain.function.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.utils.PostGet;
import com.httpqqrobot.utils.StampToTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@ChainSequence(0)
public class GroupRecall implements FunctionAct {

    @Override
    public void act(JSONObject json, HttpServletResponse resp) {
        try {
            String notice_type = json.getString("notice_type");
            //是否发生了消息撤回
            if (ObjectUtil.notEqual(notice_type, "group_recall")) {
                return;
            }
            int message_id;
            String message;
            int group_id;
            String qq;
            String time;
            JSONObject answer = new JSONObject();
            message_id = Integer.parseInt(json.get("message_id").toString());
            answer.put("message_id", message_id);
            String res = PostGet.sendPost("http://" + AppConstant.proxy + "/get_msg", answer);
            answer = JSONObject.parseObject(res);
            message = answer.getJSONObject("data").getString("message");
            group_id = Integer.parseInt(json.get("group_id").toString());
            qq = json.get("user_id").toString();
            time = json.get("time").toString();
            answer = new JSONObject();
            answer.put("group_id", group_id);
            answer.put("message", "[CQ:at,qq=" + qq + "]在" + StampToTime.get(time) + "撤回消息:\n" + message);
            PostGet.sendPost("http://" + AppConstant.proxy + "/send_group_msg", answer);
        } catch (Exception e) {
            log.info("群消息回复异常: {}", e.getMessage());
        }
    }
}
