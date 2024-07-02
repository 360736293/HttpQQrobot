package com.httpqqrobot.function.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.function.FunctionAct;
import com.httpqqrobot.utils.SendGetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@ChainSequence(2)
public class FlashImage implements FunctionAct {

    @Override
    public void act(JSONObject json, HttpServletResponse resp) {
        try {
            //是否开启了防闪照功能
            if (ObjectUtil.notEqual(AppConstant.FlashImageStatus, AppConstant.TRUE)) {
                return;
            }
            String message = json.getString("message");
            //是否有人发闪照
            if (!(message.contains("flash") && message.contains("CQ:image"))) {
                return;
            }
            JSONObject answer = new JSONObject();
            message = message.replace("flash", "");
            answer.put("reply", message);
            SendGetMessage.sendMessage(answer, resp);
        } catch (IOException e) {
            log.info("闪照回复异常: {}", e.getMessage());
        }
    }
}
