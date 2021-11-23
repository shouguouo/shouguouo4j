package com.shouguouo.spring.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shouguouo
 * @date 2021-11-22 16:18:56
 */
@org.springframework.context.annotation.Configuration
@ComponentScan("com.shouguouo.spring.async")
@EnableAsync
public class Configuration {

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setKeepAliveSeconds(10);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        // executor.setThreadNamePrefix("ThreadPoolTaskExecutor-");
        executor.setThreadFactory(new ThreadFactory() {
            private final AtomicInteger threadCount = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("ThreadPoolTaskExecutor-" + threadCount.incrementAndGet());
                return thread;
            }
        });
        return executor;
    }
}
