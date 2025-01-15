package com.httpqqrobot.job;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.SteamDiscountNotify;
import com.httpqqrobot.service.ISteamDiscountNotifyService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class HttpQQrobotJob {

    @Resource
    private ISteamDiscountNotifyService steamDiscountNotifyServiceImpl;

    @XxlJob("steamDiscountNotify")
    public void steamDiscountNotify() {
        log.info("steamDiscountNotify 执行开始...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<SteamDiscountNotify> list = steamDiscountNotifyServiceImpl.list();
        for (SteamDiscountNotify steamDiscountNotify : list) {
            try {
                //依次处理每一个用户的订阅记录
                String userId = steamDiscountNotify.getUserId();
                String gameId = steamDiscountNotify.getGameId();
                String url = steamDiscountNotify.getUrl();
                String body = HttpRequest
                        .get(url)
                        .setHttpProxy(AppConstant.proxyIP, AppConstant.proxyPort)
                        .execute()
                        .body();
                String regexp = "discount_prices";
                String res = ReUtil.get(regexp, body, 0);
                if (StringUtils.isNotEmpty(res)) {
                    //该游戏已打折，通知用户

                    //删除改游戏记录
                    steamDiscountNotifyServiceImpl.lambdaUpdate().eq(SteamDiscountNotify::getGameId, gameId).remove();
                }
            } catch (Exception e) {
                log.error("用户:{} 游戏:{} 通知异常:{}", steamDiscountNotify.getUserId(), steamDiscountNotify.getGameId(), e.getMessage());
            }
        }
        stopWatch.stop();
        log.info("steamDiscountNotify 执行结束... 耗时: {}s", stopWatch.getLastTaskTimeMillis() / 1000);
    }

}
