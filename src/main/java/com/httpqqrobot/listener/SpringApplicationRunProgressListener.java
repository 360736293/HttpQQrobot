//package com.httpqqrobot.listener;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.SpringApplicationRunListener;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.Ordered;
//import org.springframework.core.env.ConfigurableEnvironment;
//
///**
// * SpringBoot应用程序启动流程各阶段监听器，暂时无用
// */
//@Slf4j
//public class SpringApplicationRunProgressListener implements SpringApplicationRunListener, Ordered {
//
//    public SpringApplicationRunProgressListener(SpringApplication application, String[] args) {
//        log.info("SpringApplicationRunProgressListener 实例化");
//    }
//
//    @Override
//    public void starting() {
//        log.info("SpringBoot 开始启动");
//    }
//
//    @Override
//    public void environmentPrepared(ConfigurableEnvironment environment) {
//        log.info("SpringBoot 运行环境已准备好");
//    }
//
//    @Override
//    public void contextPrepared(ConfigurableApplicationContext context) {
//        log.info("SpringBoot 上下文已准备好");
//    }
//
//    @Override
//    public void contextLoaded(ConfigurableApplicationContext context) {
//        log.info("SpringBoot 上下文已加载");
//    }
//
//    @Override
//    public void started(ConfigurableApplicationContext context) {
//        log.info("SpringBoot 启动完成");
//    }
//
//    @Override
//    public void running(ConfigurableApplicationContext context) {
//        log.info("SpringBoot 运行中");
//    }
//
//    @Override
//    public void failed(ConfigurableApplicationContext context, Throwable exception) {
//        log.info("SpringBoot 启动失败");
//    }
//
//    @Override
//    public int getOrder() {
//        return 1;
//    }
//}
