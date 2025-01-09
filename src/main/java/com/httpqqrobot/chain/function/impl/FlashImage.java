package com.httpqqrobot.chain.function.impl;

import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.chain.function.FunctionAct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlashImage implements FunctionAct {

    @Override
    public void act(JSONObject json) {
        try {
//            String message = json.getString("message");
//            //是否有人发闪照
//            if (!(message.contains("flash") && message.contains("CQ:image"))) {
//                return;
//            }
//            JSONObject answer = new JSONObject();
//            message = message.replace("flash", "");
//            answer.put("reply", message);
//            SendGetMessage.sendMessage(answer, resp);
        } catch (Exception e) {
            log.info("闪照回复异常: {}", e.getMessage());
        }
    }
}
