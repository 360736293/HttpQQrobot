package com.httpqqrobot.config;

import cn.hutool.core.util.ObjectUtil;
import com.httpqqrobot.annotation.ChainSequence;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.function.FunctionAct;
import com.httpqqrobot.utils.LoadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Component
@Slf4j
public class InitializationApp implements ApplicationRunner {
    @Resource
    private LoadConfig loadConfig;
    @Resource
    private FunctionHandlerChain functionHandlerChain;

    @Override
    public void run(ApplicationArguments args) {
        try {
            //按照类上注解的顺序装配com.httpqqrobot.function.impl包下所有的处理器链条
            assembleFunctionHandlerChain();
            //加载配置文件
            loadConfig.act();
            log.info("初始化完成");
        } catch (Exception e) {
            log.info("初始化失败: {}", e.getMessage());
        }
    }

    public void assembleFunctionHandlerChain() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String packageName = "com.httpqqrobot.function.impl";
        String packagePath = packageName.replace('.', '/');
        List<Class> classes = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String classPath = classLoader.getResource(packagePath).getFile();
        File packageDirectory = new File(classPath);

        if (packageDirectory.exists() && packageDirectory.isDirectory()) {
            File[] files = packageDirectory.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".class")) {
                    String className = packageName + '.' + fileName.substring(0, fileName.lastIndexOf('.'));
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        }
        TreeMap<Integer, Object> sortedMap = new TreeMap<>();
        for (Class aClass : classes) {
            //获取类上@ChainSequence注解
            ChainSequence chainSequence = (ChainSequence) aClass.getAnnotation(ChainSequence.class);
            if (ObjectUtil.isEmpty(chainSequence)) {
                continue;
            }
            Integer sequence = chainSequence.value();
            Object classObject = aClass.newInstance();
            sortedMap.put(sequence, classObject);
        }
        for (Integer sequence : sortedMap.keySet()) {
            functionHandlerChain.addHandler((FunctionAct) sortedMap.get(sequence));
        }
    }
}
