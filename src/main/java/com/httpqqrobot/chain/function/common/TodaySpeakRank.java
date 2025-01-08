package com.httpqqrobot.chain.function.common;

import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.service.IUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TodaySpeakRank {
    @Resource
    private IUserMessageService userMessageService;

    public void act(JSONObject json, HttpServletResponse resp) {
        try {
//            String qq = json.getJSONObject("sender").getString("user_id");
//            String group_id = json.getString("group_id");
//
//            JSONObject answer = new JSONObject();
//            JSONObject answerContent = new JSONObject();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String date = simpleDateFormat.format(new Date());
//            List<UserMessage> todayUserSpeakRank = userMessageService.getTodayUserSpeakRank(group_id, date);
//            StringBuilder res = new StringBuilder("[CQ:at,qq=" + qq + "]\n");
//            res.append(date).append("  今日发言前十关键字:\n\n");
//
//            StringBuilder content = new StringBuilder();
//            List<String> todayUserSpeakContent = userMessageService.getTodayUserSpeakContent(group_id, date);
//            for (String s : todayUserSpeakContent) {
//                content.append(s);
//            }
//            answerContent.put("content", content.toString());
//            //go-cqhttp内置的分词
//            String s = PostGet.sendPost("http://" + AppConstant.proxy + "/.get_word_slices", answerContent);
//            JSONObject jsonObject = JSONObject.parseObject(s);
//            String slices = jsonObject.getJSONObject("data").getString("slices");
//            String[] keys = slices.split(",");
//            TreeMap<String, Integer> treeMap = new TreeMap<>();
//            StringBuilder resKeys = new StringBuilder();
//            for (String key : keys) {
//                key = key.replaceAll("[^\\u4e00-\\u9fa5]", "");
//                //单个字的直接忽视
//                if (key.length() >= 2) {
//                    //预计排除的词
//                    if (AppConstant.excludeWordsList.contains(key)) {
//                        continue;
//                    }
//                    Integer num = treeMap.get(key);
//                    treeMap.put(key, num == null ? 1 : num + 1);
//                }
//            }
//            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(treeMap.entrySet());
//            list.sort((map1, map2) -> {
//                Integer value1 = map1.getValue();
//                Integer value2 = map2.getValue();
//                return value2.compareTo(value1);
//            });
//            int i = 0;
//            for (Map.Entry<String, Integer> m : list) {
//                i++;
//                if (i > 10) break;
//                resKeys.append(m.getKey()).append("  ");
//            }
//            res.append(resKeys).append("\n\n");
//            int index = 1;
//            res.append("前五发言排名:\n");
//            for (UserMessage userMessage : todayUserSpeakRank) {
//                res.append("排名：").append(index).append("    账号：").append(userMessage.getQqNumber()).append("    昵称：").append(userMessage.getQqName()).append("    发言数：").append(userMessage.getSum()).append("\n");
//                index++;
//            }
//            answer.put("reply", res.toString());
//            SendGetMessage.sendMessage(answer, resp);
        } catch (Exception e) {
            log.info("获取今天发言排名异常: {}", e.getMessage());
        }
    }
}
