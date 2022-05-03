package com.shouguouo.demo.nio.reactor;

import lombok.NonNull;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shouguouo
 * @date 2022-05-03 14:57:48
 */
public class ReactorTry {

    public static void main(String[] args) {
        // 业务线程池
        ExecutorService business = new ThreadPoolExecutor(
                10,
                50,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(1000),
                new ThreadFactory() {
                    private static final String NAME = "shouguouo-business-pool-%s";

                    private final AtomicInteger poolNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(String.format(NAME, poolNumber.getAndIncrement()));
                        return thread;
                    }
                });
        MainReactorGroup mainReactorGroup = new MainReactorGroup();
        SubReactorGroup subReactorGroup = new SubReactorGroup(business);
        new Acceptor(new InetSocketAddress(9080), mainReactorGroup, subReactorGroup).start();
    }
}
