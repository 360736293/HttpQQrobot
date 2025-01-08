package com.httpqqrobot;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.chain.function.FunctionAct;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.UserAuthority;
import com.httpqqrobot.service.IUserAuthorityService;
import com.httpqqrobot.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
@SpringBootApplication
@EnableScheduling
@NacosPropertySource(dataId = "excludeWords-dev.properties", autoRefreshed = true, groupId = "DEFAULT_GROUP")
public class HttpQQrobotApplication implements ApplicationRunner {

    @NacosValue(value = "${excludeWordsString}", autoRefreshed = true)
    private String excludeWordsString;
    @Resource
    private FunctionHandlerChain functionHandlerChain;
    @Resource
    private IUserAuthorityService userAuthorityService;

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
            if (ObjectUtil.isNotEmpty(excludeWordsString) && ObjectUtil.notEqual(excludeWordsString, "null")) {
                AppConstant.excludeWordsList = Arrays.asList(excludeWordsString.split(","));
            }
            log.info("初始化完成");
        } catch (Exception e) {
            log.info("初始化失败: {}", e.getMessage());
        }
    }

    public void assembleFunctionHandlerChain() throws InstantiationException, IllegalAccessException {
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
            RedisUtils.set("UserId:" + userAuthority.getUserId(), userAuthority.getRole());
        }
    }
}
