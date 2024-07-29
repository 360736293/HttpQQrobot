package com.httpqqrobot;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.UserAuthority;
import com.httpqqrobot.service.IUserAuthorityService;
import com.httpqqrobot.utils.LoadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@SpringBootApplication
@EnableScheduling
@NacosPropertySource(dataId = "excludeWords-dev.properties", autoRefreshed = true, groupId = "DEFAULT_GROUP")
public class HttpQQrobotApplication implements ApplicationRunner, ApplicationContextAware {

    @NacosValue(value = "${excludeWordsString}", autoRefreshed = true)
    private String excludeWordsString;
    @Resource
    private LoadConfig loadConfig;
    @Resource
    private FunctionHandlerChain functionHandlerChain;
    @Resource
    private IUserAuthorityService userAuthorityService;

    List<Class<FunctionAct>> classes = new ArrayList<>();

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
            //加载配置文件
            //loadConfig.act();
            //加载排除词
            if (ObjectUtil.isNotEmpty(excludeWordsString) && ObjectUtil.notEqual(excludeWordsString, "null")) {
                AppConstant.excludeWordsList = Arrays.asList(excludeWordsString.split(","));
            }
            log.info("初始化完成");
        } catch (Exception e) {
            log.info("初始化失败: {}", e.getMessage());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, FunctionAct> beansOfType = applicationContext.getBeansOfType(FunctionAct.class);
        for (String s : beansOfType.keySet()) {
            classes.add((Class<FunctionAct>) beansOfType.get(s).getClass());
        }
    }

    public void assembleFunctionHandlerChain() throws InstantiationException, IllegalAccessException {
        TreeMap<Integer, FunctionAct> sortedMap = new TreeMap<>();
        for (Class<FunctionAct> aClass : classes) {
            //获取类上@ChainSequence注解
            ChainSequence chainSequence = aClass.getAnnotation(ChainSequence.class);
            if (ObjectUtil.isEmpty(chainSequence)) {
                continue;
            }
            Integer sequence = chainSequence.value();
            FunctionAct functionAct = aClass.newInstance();
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
}
