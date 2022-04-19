package com.shouguouo.common.util;

import lombok.experimental.UtilityClass;

import java.io.PrintStream;

/**
 * @author shouguouo
 * @date 2021-11-28 12:56:03
 */
@UtilityClass
public class OutputUtils {

    public void printlnWithCurrentThread(String message, Level level) {
        Level.getPrintStream(level).println(Thread.currentThread().getName() + ": " + message);
    }

    public void cuttingLine(String message) {
        System.out.printf("-------------------%s-------------------%n", message);
    }

    public void printlnWithCurrentThread(String message) {
        printlnWithCurrentThread(message, Level.INFO);
    }

    public void printStackTraceWithCurrentThread(Throwable throwable) {
        PrintStream printStream = Level.getPrintStream(Level.ERROR);
        printStream.print(Thread.currentThread().getName() + ": ");
        throwable.printStackTrace(printStream);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void printf(String message, Object... args) {
        System.out.printf(message, args);
        System.out.println();
    }

    public enum Level {
        /**
         * 级别
         */
        INFO,
        ERROR,
        ;

        private static PrintStream getPrintStream(Level level) {
            if (level == ERROR) {
                return System.err;
            }
            return System.out;
        }
    }

    public void printWithCurrentThread(String message) {
        System.out.print(Thread.currentThread().getName() + ": " + message);
    }

}
