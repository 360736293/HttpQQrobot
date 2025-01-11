package com.httpqqrobot;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.UserAuthority;
import com.httpqqrobot.service.IUserAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executor;

@Slf4j
@SpringBootApplication
public class HttpQQrobotApplication implements ApplicationRunner {

    @Resource
    private FunctionHandlerChain functionHandlerChain;

    @Resource
    private IUserAuthorityService userAuthorityService;

    @Resource
    private ConfigService nacosConfigService;

    @Value("${nacos.config.readConfigTimeout}")
    private long readConfigTimeout;

    @Value("${robot.ip}")
    private String robotIp;

    @Value("${robot.qq}")
    private String robotQQ;

    @Value("${tongyiqianwen.apikey}")
    private String tongyiqianwenApiKey;

    @Value("${http-https.proxy.ip}")
    private String proxyIP;

    @Value("${http-https.proxy.port}")
    private int proxyPort;

    public static void main(String[] args) {
        SpringApplication.run(HttpQQrobotApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            //按照类上注解的顺序装配FunctionAct下面的实现类处理器链条
            assembleFunctionHandlerChain();
            //加载用户权限数据
            loadUserAuthorityData();
            //加载排除词
            String excludeWordsString = nacosConfigService.getConfigAndSignListener("excludeWords-dev", "DEFAULT_GROUP", readConfigTimeout, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String excludeWordsString) {
                    //更新排除词
                    if (ObjectUtil.isNotEmpty(excludeWordsString)) {
                        AppConstant.excludeWords = Arrays.asList(excludeWordsString.split("\n"));
                    }
                }
            });
            if (ObjectUtil.isNotEmpty(excludeWordsString)) {
                AppConstant.excludeWords = Arrays.asList(excludeWordsString.split("\n"));
            }
            //加载通义千问提示词
            String promptWords = nacosConfigService.getConfigAndSignListener("promptWords-dev", "DEFAULT_GROUP", readConfigTimeout, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String promptWords) {
                    //更新提示词
                    if (ObjectUtil.isNotEmpty(promptWords)) {
                        String[] list = promptWords.split("\n");
                        for (String s : list) {
                            AppConstant.promptWords += s;
                        }
                    }
                }
            });
            if (ObjectUtil.isNotEmpty(promptWords)) {
                String[] list = promptWords.split("\n");
                for (String s : list) {
                    AppConstant.promptWords += s;
                }
            }
            //加载基础数据
            String commonConfig = nacosConfigService.getConfigAndSignListener("common-dev", "DEFAULT_GROUP", readConfigTimeout, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String commonConfig) {
                    //更新基础数据
                    loadCommonConfig(commonConfig);
                }
            });
            loadCommonConfig(commonConfig);
            //赋值机器人IP地址
            AppConstant.robotIp = robotIp;
            //赋值机器人QQ
            AppConstant.robotQQ = robotQQ;
            //赋值通义千问APIKEY
            AppConstant.tongyiqianwenApiKey = tongyiqianwenApiKey;
            //赋值http-https代理
            AppConstant.proxyIP = proxyIP;
            AppConstant.proxyPort = proxyPort;
            log.info("初始化完成");
        } catch (Exception e) {
            log.info("初始化失败: {}", e.getMessage());
        }
    }

    public void assembleFunctionHandlerChain() {
        //获取目标路径下所有有指定注解的类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation("com.httpqqrobot.chain.function.impl", ChainSequence.class);
        TreeMap<Integer, FunctionAct> sortedMap = new TreeMap<>();
        for (Class<?> aClass : classes) {
            //获取类上@ChainSequence注解
            ChainSequence chainSequence = aClass.getAnnotation(ChainSequence.class);
            if (ObjectUtil.isEmpty(chainSequence)) {
                continue;
            }
            Integer sequence = chainSequence.value();
            FunctionAct functionAct = (FunctionAct) SpringUtil.getBean(aClass);
            sortedMap.put(sequence, functionAct);
        }
        for (Integer sequence : sortedMap.keySet()) {
            functionHandlerChain.addHandler(sortedMap.get(sequence));
        }
    }

    public void loadUserAuthorityData() {
        List<UserAuthority> userAuthorityList = userAuthorityService.list();
        for (UserAuthority userAuthority : userAuthorityList) {
            AppConstant.userAuthorityMap.put(userAuthority.getUserId(), userAuthority.getRole());
        }
    }

    public void loadCommonConfig(String commonConfig) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlConfig = yaml.load(commonConfig);
        //赋值通义千问模型
        AppConstant.tongyiqianwenModel = ((Map<String, Object>) ((Map<String, Object>) yamlConfig.get("robot")).get("tongyiqianwen")).get("model").toString();
        //赋值通义千问最大上下文数量（双方合计）
        AppConstant.tongyiqianwenMaxContextCount = Integer.parseInt(((Map<String, Object>) ((Map<String, Object>) yamlConfig.get("robot")).get("tongyiqianwen")).get("maxContextCount").toString());
    }
}
