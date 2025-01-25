package com.httpqqrobot.controller;


import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson2.JSONObject;
import com.httpqqrobot.annotation.RateLimit;
import com.httpqqrobot.chain.FunctionHandlerChain;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.result.Result;
import com.httpqqrobot.result.ResultInfoEnum;
import com.httpqqrobot.utils.RequestHolderUtil;
import com.httpqqrobot.utils.SignatureValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class HttpQQrobotMainController {
    @Resource
    private FunctionHandlerChain functionHandlerChain;

    /**
     * QQ机器人请求入口
     */
    @RateLimit(limit = 5)
    @PostMapping("/napcat")
    public Result napcat(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = RequestHolderUtil.get();
        log.info("input parameter: {}", json.toJSONString());
        functionHandlerChain.doHandler(json);
        RequestHolderUtil.remove();
        return Result.success(ResultInfoEnum.SUCCESS.getCode(), ResultInfoEnum.SUCCESS.getMsg(), null);
    }


    /**
     * 微信服务号请求入口
     */
    @PostMapping("/wechatService")
    public void wechatService(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String token = AppConstant.wechatServiceToken;
            String timestamp = req.getParameter("timestamp");
            String nonce = req.getParameter("nonce");
            String signature = req.getParameter("signature");
            //验证签名
            boolean isValid = SignatureValidatorUtil.validateSignature(token, timestamp, nonce, signature);
            if (isValid) {
                //签名验证通过，执行业务逻辑
                Document document = XmlUtil.readXML(req.getInputStream());
                log.info("input parameter: {}", document.getTextContent());
            } else {
                //签名验证失败
                log.info("伪造数据，不予理会");
            }
        } catch (Throwable e) {
            log.error("微信服务号请求入口异常: ", e);
        }
    }
}