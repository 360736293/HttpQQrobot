package com.example.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstant {

    public static String TRUE = "1";
    public static String FALSE = "0";
    public static String GroupRecallStatus;
    public static String FlashImageStatus;
    public static String PokeStatus;
    public static String TodaySpeakRank;


    //当前充当机器人的QQ号，会在InitializationApp里进行初始化
    public static String robotQQ;
    //机器人反向代理服务器的地址
    public static String proxy;


    @Value("${Robot.qq}")
    public void setRobotQQ(String robotQQ) {
        AppConstant.robotQQ = robotQQ;
    }

    @Value("${Robot.proxy}")
    public void setProxy(String proxy) {
        AppConstant.proxy = proxy;
    }
}
