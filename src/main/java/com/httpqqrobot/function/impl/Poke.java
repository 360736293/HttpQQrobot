package com.httpqqrobot.function.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.function.FunctionAct;
import com.httpqqrobot.utils.PostGet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class Poke implements FunctionAct {

    public void act(JSONObject json, HttpServletResponse resp) {
        try {
            //是否开启了戳一戳功能
            if (ObjectUtil.notEqual(AppConstant.PokeStatus, AppConstant.TRUE)) {
                return;
            }
            String sub_type = json.getString("sub_type");
            //是否发生了戳一戳
            if (ObjectUtil.notEqual(sub_type, "poke")) {
                return;
            }
            String qq = null;
            Integer group_id = null;
            JSONObject answer = new JSONObject();
            qq = json.getString("target_id");
            if (qq.equals(json.getString("self_id"))) {
                qq = json.getString("user_id");
                group_id = Integer.parseInt(json.get("group_id").toString());
                answer.put("group_id", group_id);
                answer.put("message", "[CQ:record,file=https://njc-download.ftn.qq.com/ftn_handler/400f36e03abe6df0ac038f2e3e623a02c3e0acd344453ed46371403af37fc3f157297f90b3db27d9e5b3d416dd3ad462ded405dec2bb78dae969da1afa142101/?fname=1.mp3&amp;xffz=172800&amp;k=cdcc60341703b820ffbf1834306538353290343432653835171c075701070a0004001b5602510118060503061f5d0f04041e575257565e535102035656573d35031d5b440171aebe2b84f6c7c267f7c45bd18cd99cb7155f72a1&amp;code=23642e85]");
                PostGet.sendPost("http://" + AppConstant.proxy + "/send_group_msg", answer);
            }
        } catch (Exception e) {
            log.info("戳一戳回复异常: {}", e.getMessage());
        }
    }
}