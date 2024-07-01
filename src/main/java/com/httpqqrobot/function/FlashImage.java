package com.httpqqrobot.function;

import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.utils.SendGetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class FlashImage {

    public void act(String message, HttpServletResponse resp) {
        try {
            JSONObject answer = new JSONObject();
            message = message.replace("flash", "");
            answer.put("reply", message);
            SendGetMessage.sendMessage(answer, resp);
        } catch (IOException e) {
            log.info("闪照回复异常: {}", e.getMessage());
        }
    }
}
