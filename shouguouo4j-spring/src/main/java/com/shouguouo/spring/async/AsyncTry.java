package com.shouguouo.spring.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author shouguouo
 * @date 2021-11-22 16:18:04
 */

public class AsyncTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        AsyncService asyncService = context.getBean(AsyncService.class);
        printCost(asyncService::doBusinessSync);
        printCost(asyncService::doBusinessAsync);
        List<CompletableFuture<String>> cfList = printCost(asyncService::doBusinessWithCompletableFuture);
        for (CompletableFuture<String> cf : cfList) {
            cf.whenComplete((x, e) -> {
                System.out.println(Thread.currentThread().getName() + " Completed: " + x);
            });
        }
    }

    public static void printCost(Consumer consumer) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            consumer.consume();
        }
        System.out.println("cost: " + (System.currentTimeMillis() - start) / 1000.0 + "s");
    }

    public static <T> List<T> printCost(Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        List<T> resultList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            resultList.add(supplier.get());
        }
        System.out.println("cost: " + (System.currentTimeMillis() - start) / 1000.0 + "s");
        return resultList;
    }

    @FunctionalInterface
    public interface Consumer {

        /**
         * 消费
         */
        void consume();
    }
}
