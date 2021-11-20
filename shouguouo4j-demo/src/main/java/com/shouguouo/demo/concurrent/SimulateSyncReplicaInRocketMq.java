package com.shouguouo.demo.concurrent;

import lombok.experimental.UtilityClass;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author shouguouo
 * @date 2021-11-20 20:15:04
 */
@UtilityClass
public class SimulateSyncReplicaInRocketMq {

    private long start;

    public Runnable runBlocked() {
        return () -> {
            Consumer<String> sb = (x) -> System.out.println(Thread.currentThread().getName() + ":" + x);
            // blocked
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            sb.accept("synchronized completed in " + (System.currentTimeMillis() - start) / 1000.0 + "s");
        };
    }

    public Runnable runWithCompletableFuture(ThreadPoolExecutor executor) {
        return () -> {
            Consumer<String> sb = (x) -> System.out.println(Thread.currentThread().getName() + ":" + x);
            // use CompletableFuture
            CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
                return "CompletableFuture completed in " + (System.currentTimeMillis() - start) / 1000.0 + "s";
            }, executor).thenAcceptAsync(sb, executor);
        };
    }

    /**
     * output:
     * synchronized completed in 3.113s
     * just run in 3.14s
     * CompletableFuture completed in 6.145s
     *
     * @param args none
     */
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10));
        start = System.currentTimeMillis();
        executor.execute(runBlocked());
        // blocked here because executor's core size is 1
        executor.execute(runWithCompletableFuture(executor));
        // unblocked，CompletableFuture async
        executor.execute(() -> System.out.println(Thread.currentThread().getName() + " just run in " + (System.currentTimeMillis() - start) / 1000.0 + "s"));

        // waiting for runWithCompletableFuture completed
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

}
