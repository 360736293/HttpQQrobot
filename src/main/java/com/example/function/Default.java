package com.example.function;

import com.alibaba.fastjson.JSONObject;
import com.example.utils.SendGetMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class Default {

    public void act(String qq, HttpServletResponse resp) {
        try {
            JSONObject answer = new JSONObject();
            answer.put("reply", "[CQ:at,qq=" + qq + "]我不识别你的指令请\"@\"我输入\"菜单\"查询");
            SendGetMessage.sendMessage(answer, resp);
        } catch (Exception e) {
            log.info("默认回复异常: {}", e.getMessage());
        }
    }
}
