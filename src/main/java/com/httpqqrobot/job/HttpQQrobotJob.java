package com.httpqqrobot.job;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;
import com.httpqqrobot.constant.AppConstant;
import com.httpqqrobot.entity.SteamDiscountNotify;
import com.httpqqrobot.service.ISteamDiscountNotifyService;
import com.httpqqrobot.utils.RobotUtil;
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
                String gameName = steamDiscountNotify.getGameName();
                String url = steamDiscountNotify.getUrl();
                String body = HttpRequest
                        .get(url)
                        .form("Cookie", "wants_mature_content=1; birthtime=915120001; lastagecheckage=1-January-1999;")
                        .setHttpProxy(AppConstant.proxyIP, AppConstant.proxyPort)
                        .execute()
                        .body();
                String regexp = "game_purchase_discount_countdown";
                String res = ReUtil.get(regexp, body, 0);
                if (StringUtils.isNotEmpty(res)) {
                    //该游戏已打折，通知用户
                    RobotUtil.privateMessage(userId, "你订阅的游戏: " + gameName + " (" + url + ") 打折了，快去看看吧!");
                    //删除该游戏订阅记录
                    steamDiscountNotifyServiceImpl.lambdaUpdate().eq(SteamDiscountNotify::getGameId, gameId).remove();
                }
                log.info("用户:{} 游戏:{} 处理完毕", steamDiscountNotify.getUserId(), steamDiscountNotify.getGameId());
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("用户:{} 游戏:{} 通知异常:", steamDiscountNotify.getUserId(), steamDiscountNotify.getGameId(), e);
            }
        }
        stopWatch.stop();
        log.info("steamDiscountNotify 执行结束... 耗时: {}s", stopWatch.getLastTaskTimeMillis() / 1000);
    }

}
