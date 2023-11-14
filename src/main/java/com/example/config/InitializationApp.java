package com.example.config;

import com.example.utils.FunctionStatusLoad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class InitializationApp implements ApplicationRunner {
    @Resource
    private FunctionStatusLoad functionStatusLoad;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            functionStatusLoad.act();
            log.info("初始化完成");
        } catch (Exception e) {
            log.info("初始化失败: {}", e.getMessage());
        }
    }
}
