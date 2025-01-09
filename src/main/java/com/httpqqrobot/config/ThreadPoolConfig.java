package com.httpqqrobot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Value("${threadPool.corePoolSize}")
    private int corePoolSize;

    @Value("${threadPool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${threadPool.keepAliveTime}")
    private int keepAliveTime;
    private final TimeUnit timeUnit = TimeUnit.SECONDS;
    private BlockingQueue<Runnable> workQueue;
    private final ThreadFactory threadFactory = Executors.defaultThreadFactory();
    private final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

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
