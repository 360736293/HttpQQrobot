package com.httpqqrobot.constant;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppConstant {

    public static String TRUE = "1";

    public static String FALSE = "0";

    //排除词
    public static List<String> excludeWordsList = new ArrayList<>();

    //通义千问提示词
    public static String promptWordsString = "";

    //机器人反向代理服务器的IP地址
    public static String robotIp;

    //机器人QQ
    public static String robotQQ;

    //通义千问APIkey
    public static String tongyiqianwenApiKey;
}
