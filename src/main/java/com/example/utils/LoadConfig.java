package com.example.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import com.example.constant.AppConstant;
import com.example.entity.FunctionStatus;
import com.example.service.IFunctionStatusService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class LoadConfig {
    @Resource
    private IFunctionStatusService functionStatusService;

    @Value("${robot.configPath}")
    private String configPath;

    public void act() {
        //加载配置文件内容
        StringBuilder sb = new StringBuilder();
        FileUtil.readLines(configPath, StandardCharsets.UTF_8).forEach(line -> {
            sb.append(line).append("\n");
        });
        String config = sb.toString();
        AppConstant.robotQQ = ReUtil.get("\\suin: (.*?) ", config, 1);
        AppConstant.proxy = ReUtil.get("\\saddress: (.*?) ", config, 1);
        //加载数据库数据
        List<FunctionStatus> allFunctionStatus = functionStatusService.list();
        for (FunctionStatus functionStatus : allFunctionStatus) {
            switch (functionStatus.getName()) {
                case "GroupRecallStatus":
                    AppConstant.GroupRecallStatus = functionStatus.getStatus();
                    break;
                case "FlashImageStatus":
                    AppConstant.FlashImageStatus = functionStatus.getStatus();
                    break;
                case "PokeStatus":
                    AppConstant.PokeStatus = functionStatus.getStatus();
                    break;
                case "TodaySpeakRank":
                    AppConstant.TodaySpeakRank = functionStatus.getStatus();
                    break;
                default:
            }
        }
    }
}
