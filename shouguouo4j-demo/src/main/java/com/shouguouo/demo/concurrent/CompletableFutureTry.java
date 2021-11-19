package com.shouguouo.demo.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFutureTry
 *
 * @author shouguouo
 * @date 2021-11-18 16:18:07
 */
public class CompletableFutureTry {

    /**
     * 入参维度
     * <ul>
     *     <li>apply 接口的参数是(Bi)Function类型 使用上一个stage的结果作为入参 产生新结果。</li>
     *     <li>accept 接口的参数是(Bi)Consumer类型 使用上一个stage的结果作为入参 但不对结果产生影响。</li>
     *     <li>run 不依赖上一stage的结果，只要上一阶段完成就执行指定的结果，且不对结果产生影响。</li>
     * </ul>
     * 依赖维度
     * <ul>
     *     <li>then 依赖前个阶段</li>
     *     <li>combine/both 由完成两个阶段而触发，可以结合他们的结果或产生的影响</li>
     *     <li>either 由两个阶段任意一个完成触发，不能保证哪个结果或效果用于相关阶段的计算</li>
     * </ul>
     * 执行方式
     * <ul>
     *     <li>默认方式</li>
     *     <li>异步方式 async后缀，但没有Executor参数</li>
     *     <li>自定义方式 async后缀，有Executor参数</li>
     * </ul>
     * 上阶段完成状态
     * <ul>
     *     <li>whenComplete 消费型接口</li>
     *     <li>handle 有上一阶段产生的结果/异常 产出新结果</li>
     *     <li>exceptionally 上一阶段异常完成时处理，根据上一阶段异常产生新结果</li>
     * </ul>
     *
     * @see CompletionStage
     */

    public static void testApply() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("1");
        CompletableFuture<String> cf2 = CompletableFuture.completedFuture("2");
        cf.thenApply(Integer::valueOf).thenApplyAsync(x -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return String.valueOf(x);
        }).applyToEither(cf2, x -> {
            System.out.println(x); // 2
            return x;
        });
        cf.thenCombine(cf2, (x, y) -> {
            System.out.println(x + y); // 12
            return x + y;
        });
        cf.thenComposeAsync(x -> CompletableFuture.completedFuture("compose " + x)).thenAccept(System.out::println);
    }

    public static void testAccept() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("1");
        CompletableFuture<String> cf2 = CompletableFuture.completedFuture("2");
        cf.thenAccept(System.out::println);
        cf.thenAcceptAsync(x -> System.out.println("2"));
        cf.thenApply(x -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return x;
        }).thenAcceptBoth(cf2, (x, y) -> System.out.println(x + y));
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "3";
        }).acceptEitherAsync(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "4";
        }), System.out::println);
    }

    public static void testRun() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("1");
        cf.thenRunAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("runAsync");
        }).thenRun(() -> System.out.println("thenRun"));
        cf.thenRunAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("both 1");
        }).runAfterBothAsync(CompletableFuture.supplyAsync(() -> 1), () -> System.out.println("both completed"));
        cf.thenRunAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("either 1");
        }).runAfterEitherAsync(CompletableFuture.supplyAsync(() -> 1), () -> System.out.println("either completed"));
        System.out.println("testRunEnd");
    }

    public static void testHandle() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("1");
        cf.thenApplyAsync(x -> {
            throw new UnsupportedOperationException("UnSupported");
        }).handle((x, y) -> {
            System.out.println(x);
            System.out.println(y.getMessage());
            System.out.println(y.getCause().getMessage());
            return x;
        });
        cf.thenApplyAsync(x -> "2").handle((x, y) -> {
            System.out.println(x);
            System.out.println(y.getMessage());
            System.out.println(y.getCause().getMessage());
            return x;
        });
    }

    public static void testExceptionallyAndCompleted() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("1");
        cf.thenApplyAsync(x -> x).exceptionally(x -> {
            System.out.println(x.getMessage());
            System.out.println(x.getCause().getMessage());
            return "Exceptionally 1 ";
        });
        cf.thenApplyAsync(x -> {
            throw new UnsupportedOperationException("UnSupported 2");
        }).exceptionally(x -> {
            System.out.println(x.getMessage());
            System.out.println(x.getCause().getMessage());
            return "Exceptionally 2";
        });
        cf.thenApplyAsync(x -> x).whenCompleteAsync((x, y) -> {
            System.out.println(x);
            System.out.println(y.getMessage());
            System.out.println(y.getCause().getMessage());
        });
        cf.thenApplyAsync(x -> {
            throw new UnsupportedOperationException("UnSupported 3");
        }).whenCompleteAsync((x, y) -> {
            System.out.println(x);
            System.out.println(y.getMessage());
            System.out.println(y.getCause().getMessage());
        });

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-------------apply-----------");
        testApply();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("-------------accept-----------");
        testAccept();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("-------------run-----------");
        testRun();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("-------------handle-----------");
        testHandle();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("-------------exceptionally and completed-----------");
        testExceptionallyAndCompleted();
        TimeUnit.SECONDS.sleep(5);

    }
}
