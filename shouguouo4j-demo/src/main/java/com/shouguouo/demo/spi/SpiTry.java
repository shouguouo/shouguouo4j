package com.shouguouo.demo.spi;

import java.util.ServiceLoader;

/**
 * 关于ContextClassloader
 * 前提1：对于代码中引用的类，默认使用当前类的加载类去加载
 * 前提2：SPI机制想要通过系统库类去加载用户实现类，需要获取AppClassloader加载实现类
 * Thread.currentThread().getContextClassLoader()默认返回当前线程的类加载器（AppClassloader）
 * 获取并通过该Classloader去加载SPI的实现类
 *
 * @author shouguouo
 * @date 2021-06-26 22:22:35
 */
public class SpiTry {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getContextClassLoader());
        ServiceLoader<Hi> loadedHi = ServiceLoader.load(Hi.class);
        for (Hi impl : loadedHi) {
            impl.sayHi();
        }
    }

}
