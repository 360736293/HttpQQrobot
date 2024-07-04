package com.httpqqrobot.constant;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppConstant {

    public static String TRUE = "1";
    public static String FALSE = "0";
    public static String START = "1";
    public static String STOP = "0";
    public static String GroupRecallStatus;
    public static String FlashImageStatus;
    public static String PokeStatus;
    public static String TodaySpeakRank;

    //用户权限map
    public static Map<String, String> userAuthorityMap = new HashMap<>();


    //当前充当机器人的QQ号
    public static String robotQQ;
    //机器人反向代理服务器的地址
    public static String proxy;
}
