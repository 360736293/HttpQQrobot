package com.httpqqrobot.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestJob {

    @XxlJob("testJob")
    public void testJob() {
        log.info("testJob start...");
        log.info("testJob end...");
    }

}
