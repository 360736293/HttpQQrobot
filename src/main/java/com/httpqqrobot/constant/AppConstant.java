package com.httpqqrobot.constant;

import com.httpqqrobot.entity.AIRequestBody;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AppConstant {

    public final static String INSERT = "0";
    public final static String DELETE = "1";
    public final static String UPDATE = "2";
    public final static String QUERY = "3";

    //排除词
    public static List<String> excludeWords = new ArrayList<>();

    //通义千问提示词
    public static String promptWords = "";

    //通义千问模型
    public static String tongyiqianwenModel;

    //通义千问最大上下文数量（双方合计）
    public static Integer tongyiqianwenMaxContextCount;

    //通义千问用户对话上下文，key由QQ群-QQ号组成，value是用户对话历史记录
    public static ConcurrentHashMap<String, List<AIRequestBody.Message>> chatContext = new ConcurrentHashMap<>();

    //用户权限数据
    public static ConcurrentHashMap<String, String> userAuthorityMap = new ConcurrentHashMap<>();

    //机器人反向代理服务器的IP地址
    public static String robotIp;

    //机器人QQ
    public static String robotQQ;

    //通义千问APIkey
    public static String tongyiqianwenApiKey;

    //http-https代理IP
    public static String proxyIP;

    //http-https代理端口
    public static Integer proxyPort;

    //谷歌搜索APIKEY
    public static String googleSearchApikey;

    //谷歌搜索引擎ID
    public static String googleSearchEngineID;
}
