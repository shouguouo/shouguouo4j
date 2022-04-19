package com.shouguouo.demo.concurrent;

import com.shouguouo.common.util.ExecutorUtils;
import com.shouguouo.common.util.OutputUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * <a href="https://stackoverflow.com/questions/281132/java-volatile-reference-vs-atomicreference">vs</a>
 *
 * output:
 * Volatile i: 99946
 * AtomicReference i: 100000
 *
 * @author shouguouo
 * @date 2022-04-19 21:23:46
 */
public class VolatileVsAtomicReferenceTry {

    public static void main(String[] args) throws InterruptedException {
        UsingVolatile usingVolatile = new UsingVolatile();
        UsingAtomicReference usingAtomicReference = new UsingAtomicReference();
        // 100线程 * 1000自增
        for (int i = 0; i < 100; i++) {
            ExecutorUtils.execute(usingVolatile);
            ExecutorUtils.execute(usingAtomicReference);
        }
        ExecutorUtils.shutdown();
        while (!ExecutorUtils.isTerminated()) {
        }
        usingVolatile.output();
        usingAtomicReference.output();
    }

    static class UsingVolatile implements Runnable {

        private volatile int i = 0;

        public void output() {
            OutputUtils.println("Volatile i: " + i);
        }

        @Override
        public void run() {
            for (int j = 0; j < 1000; j++) {
                i++;
            }
        }
    }

    static class UsingAtomicReference implements Runnable {

        private AtomicReference<Integer> i = new AtomicReference<>(0);

        public void output() {
            OutputUtils.println("AtomicReference i: " + i.get());
        }

        @Override
        public void run() {
            for (int j = 0; j < 1000; j++) {
                i.getAndUpdate((old) -> ++old);
            }
        }
    }
}
