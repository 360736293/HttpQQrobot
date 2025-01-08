package com.httpqqrobot.chain.function.impl;

import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.entity.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GroupRecall implements FunctionAct {

    @Override
    public void act(UserMessage userMessage) {
        try {
//            String notice_type = json.getString("notice_type");
//            //是否发生了消息撤回
//            if (ObjectUtil.notEqual(notice_type, "group_recall")) {
//                return;
//            }
//            int message_id;
//            String message;
//            int group_id;
//            String qq;
//            String time;
//            JSONObject answer = new JSONObject();
//            message_id = Integer.parseInt(json.get("message_id").toString());
//            answer.put("message_id", message_id);
//            String res = PostGet.sendPost("http://" + AppConstant.proxy + "/get_msg", answer);
//            answer = JSONObject.parseObject(res);
//            message = answer.getJSONObject("data").getString("message");
//            group_id = Integer.parseInt(json.get("group_id").toString());
//            qq = json.get("user_id").toString();
//            time = json.get("time").toString();
//            answer = new JSONObject();
//            answer.put("group_id", group_id);
//            answer.put("message", "[CQ:at,qq=" + qq + "]在" + StampToTime.get(time) + "撤回消息:\n" + message);
//            PostGet.sendPost("http://" + AppConstant.proxy + "/send_group_msg", answer);
        } catch (Exception e) {
            log.info("群消息回复异常: {}", e.getMessage());
        }
    }
}
