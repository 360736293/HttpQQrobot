package com.httpqqrobot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Value("${threadPool.corePoolSize}")
    private int corePoolSize;

    @Value("${threadPool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${threadPool.keepAliveTime}")
    private int keepAliveTime;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private BlockingQueue<Runnable> workQueue;
    private ThreadFactory threadFactory = Executors.defaultThreadFactory();
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    @Value("${threadPool.blockingQueueSize}")
    public void setWorkQueue(int blockingQueueSize) {
        this.workQueue = new ArrayBlockingQueue<>(blockingQueueSize);
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                timeUnit,
                workQueue,
                threadFactory,
                handler
        );
    }
}
