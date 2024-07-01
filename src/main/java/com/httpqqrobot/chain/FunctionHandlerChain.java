package com.httpqqrobot.chain;

import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.function.FunctionAct;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class FunctionHandlerChain {

    List<FunctionAct> handlerChain = new ArrayList<>();

    public FunctionHandlerChain addHandler(FunctionAct functionAct) {
        this.handlerChain.add(functionAct);
        return this;
    }

    public void doHandler(JSONObject json, HttpServletResponse resp) {
        for (FunctionAct functionAct : handlerChain) {
            functionAct.act(json, resp);
        }
    }
}
