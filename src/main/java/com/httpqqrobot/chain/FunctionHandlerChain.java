package com.httpqqrobot.chain;

import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.chain.function.FunctionAct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FunctionHandlerChain {

    List<FunctionAct> handlerChain = new ArrayList<>();

    public FunctionHandlerChain addHandler(FunctionAct functionAct) {
        this.handlerChain.add(functionAct);
        return this;
    }

    public void doHandler(JSONObject json) {
        for (FunctionAct functionAct : handlerChain) {
            functionAct.act(json);
        }
    }
}
