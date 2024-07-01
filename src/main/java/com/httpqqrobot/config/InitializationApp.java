package com.httpqqrobot.config;

import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.function.impl.*;
import com.httpqqrobot.utils.LoadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class InitializationApp implements ApplicationRunner {
    @Resource
    private LoadConfig loadConfig;
    @Resource
    private FunctionHandlerChain functionHandlerChain;
    @Resource
    private GroupRecall groupRecall;
    @Resource
    private Poke poke;
    @Resource
    private FlashImage flashImage;
    @Resource
    private AddUserMessage addUserMessage;
    @Resource
    private Dialogue dialogue;

    @Override
    public void run(ApplicationArguments args) {
        try {
            //装配处理器链条
            functionHandlerChain
                    .addHandler(groupRecall)
                    .addHandler(poke)
                    .addHandler(flashImage)
                    .addHandler(addUserMessage)
                    .addHandler(dialogue);
            //加载配置文件
            loadConfig.act();
            log.info("初始化完成");
        } catch (Exception e) {
            log.info("初始化失败: {}", e.getMessage());
        }
    }
}
