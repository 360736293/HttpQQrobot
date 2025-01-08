package com.httpqqrobot.chain;

import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.entity.UserMessage;
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

    public void doHandler(UserMessage userMessage) {
        for (FunctionAct functionAct : handlerChain) {
            functionAct.act(userMessage);
        }
    }
}
