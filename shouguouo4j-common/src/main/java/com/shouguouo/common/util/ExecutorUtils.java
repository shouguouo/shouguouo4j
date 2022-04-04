package com.shouguouo.common.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shouguouo
 * @date 2022-04-03 22:15:48
 */
@UtilityClass
public class ExecutorUtils {

    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
            10,
            20,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadFactory() {
                private static final String NAME = "shouguouo-thread-pool-%s";

                private final AtomicInteger poolNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName(String.format(NAME, poolNumber.getAndIncrement()));
                    return thread;
                }
            });

    public static void execute(Runnable r) {
        POOL.execute(r);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        return POOL.submit(callable);
    }

    public static void shutdown() {
        POOL.shutdown();
    }

}
