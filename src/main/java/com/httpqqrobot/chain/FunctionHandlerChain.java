package com.httpqqrobot.chain;

import com.alibaba.fastjson2.JSONObject;
import com.httpqqrobot.chain.function.FunctionHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FunctionHandlerChain {

    List<FunctionHandler> handlerChain = new ArrayList<>();

    public FunctionHandlerChain addHandler(FunctionHandler functionHandler) {
        this.handlerChain.add(functionHandler);
        return this;
    }

    public void doHandler(JSONObject json) {
        for (FunctionHandler functionHandler : handlerChain) {
            functionHandler.act(json);
        }
    }
}
