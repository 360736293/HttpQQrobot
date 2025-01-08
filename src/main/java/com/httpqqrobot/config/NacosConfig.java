package com.httpqqrobot.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.httpqqrobot.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class NacosConfig {

    @Value("${nacos.config.serverAddr}")
    private String serverAddr;

    @Value("${nacos.config.namespace}")
    private String namespace;

    @Value("${nacos.config.group}")
    private String group;

    @Value("${nacos.config.excludeWordsDataId}")
    private String excludeWordsDataId;

    @Bean
    public ConfigService nacosConfigService() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        configService.addListener(excludeWordsDataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String excludeWordsString) {
                //更新排除词
                if (ObjectUtil.isNotEmpty(excludeWordsString)) {
                    AppConstant.excludeWordsList = Arrays.asList(excludeWordsString.split("\n"));
                }
            }
        });
        return configService;
    }
}
