package com.shouguouo.demo.collection;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * compare synchronized and computeIfAbsent
 *
 * @author shouguouo
 * @date 2021-09-07 17:15:30
 */
@UtilityClass
public class ComputeIfAbsentTry {

    private final int LOOP_COUNT = 100000000;

    private final int THREAD_COUNT = 10;

    private final int ITEM_COUNT = 10;

    /**
     * 使用 ConcurrentHashMap 来统计，Key 的范围是 10。
     * 使用最多 10 个并发，循环操作 1000 万次，每次操作累加随机的 Key。
     * 如果 Key 不存在的话，首次设置值为 1。
     *
     * @return map
     * @throws InterruptedException 线程中断
     */
    private Map<String, Long> normalUse() throws InterruptedException {
        ConcurrentHashMap<String, Long> freqMap = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    // 获得一个随机的Key
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                    synchronized (freqMap) {
                        if (freqMap.containsKey(key)) {
                            // Key存在则+1
                            freqMap.put(key, freqMap.get(key) + 1);
                        } else {
                            // Key不存在则初始化为1
                            freqMap.put(key, 1L);
                        }
                    }
                }
        ));
        forkJoinPool.shutdown();
        boolean success = forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        if (!success) {
            throw new RuntimeException("计算超时");
        }
        return freqMap;
    }

    /**
     * 使用computeIfAbsent避免加锁
     *
     * @return map
     * @throws InterruptedException 线程中断
     */
    private Map<String, Long> goodUse() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> freqMap = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    // 获得一个随机的Key
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                    freqMap.computeIfAbsent(key, k -> new LongAdder()).increment();
                }
        ));
        forkJoinPool.shutdown();
        boolean success = forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        if (!success) {
            throw new RuntimeException("计算超时");
        }
        return freqMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().longValue()));
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch cd = new CountDownLatch(2);
        CompletableFuture<Map<String, Long>> cfNormal = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            try {
                return ComputeIfAbsentTry.normalUse();
            } catch (InterruptedException e) {
                System.err.println("Interrupted");
            } finally {
                System.out.println("normal cost: " + (System.currentTimeMillis() - start) / 1000.0 + "s");
                cd.countDown();
            }
            return null;
        });
        CompletableFuture<Map<String, Long>> cfGood = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            try {
                return ComputeIfAbsentTry.goodUse();
            } catch (InterruptedException e) {
                System.err.println("Interrupted");
            } finally {
                System.out.println("good cost: " + (System.currentTimeMillis() - start) / 1000.0 + "s");
                cd.countDown();
            }
            return null;
        });
        cd.await();
        Map<String, Long> normal = cfNormal.get(1, TimeUnit.MILLISECONDS);
        Map<String, Long> good = cfGood.get(1, TimeUnit.MILLISECONDS);
        System.out.println("normal value count：" + normal.values().stream().mapToLong(Long::longValue).sum());
        System.out.println("good value count：" + good.values().stream().mapToLong(Long::longValue).sum());
    }
}
