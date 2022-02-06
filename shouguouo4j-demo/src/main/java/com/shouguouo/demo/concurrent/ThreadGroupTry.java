package com.shouguouo.demo.concurrent;

/**
 * @author shouguouo
 * @date 2022-02-06 12:36:13
 */
public class ThreadGroupTry {

    public static void main(String[] args) {
        ThreadGroup mainThreadGroup = Thread.currentThread().getThreadGroup();
        System.out.println(mainThreadGroup.getName());
        ThreadGroup systemThreadGroup = mainThreadGroup.getParent();
        System.out.println(systemThreadGroup.getName());
        ThreadGroup notExist = systemThreadGroup.getParent();
        System.out.println(notExist);
        systemThreadGroup.list();
    }
}
