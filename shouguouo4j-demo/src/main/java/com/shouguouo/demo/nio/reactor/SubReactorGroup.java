package com.shouguouo.demo.nio.reactor;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IO线程组
 *
 * @author shouguouo
 * @date 2022-05-03 13:20:34
 */
public class SubReactorGroup {

    private static final int DEFAULT_NIO_THREAD_COUNT = 4;

    private final AtomicInteger counter = new AtomicInteger();

    private final SubReactor[] ioThreads;

    public SubReactorGroup(ExecutorService businessExecutor) {
        this(DEFAULT_NIO_THREAD_COUNT, businessExecutor);
    }

    public SubReactorGroup(int threads, ExecutorService businessExecutor) {
        if (threads < 1) {
            threads = DEFAULT_NIO_THREAD_COUNT;
        }
        // 初始化IO线程
        this.ioThreads = new SubReactor[threads];
        for (int i = 0; i < threads; i++) {
            ioThreads[i] = new SubReactor(businessExecutor);
            ioThreads[i].start();
        }
    }

    public void dispatch(SocketChannel channel) {
        if (channel != null) {
            next().offer(new Task(channel, SelectionKey.OP_READ, null));
        }
    }

    private SubReactor next() {
        return this.ioThreads[counter.getAndIncrement() % ioThreads.length];
    }
}
