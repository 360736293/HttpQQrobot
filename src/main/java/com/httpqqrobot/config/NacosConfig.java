package com.httpqqrobot.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Slf4j
@Configuration
public class NacosConfig {

    @Value("${nacos.config.serverAddr}")
    private String serverAddr;

    @Value("${nacos.config.namespace}")
    private String namespace;

    @Bean
    public ConfigService nacosExcludeWordsConfigService() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);
        return NacosFactory.createConfigService(properties);
    }

    @Bean
    public ConfigService nacosPromptWordsConfigService() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);
        return NacosFactory.createConfigService(properties);
    }
}
