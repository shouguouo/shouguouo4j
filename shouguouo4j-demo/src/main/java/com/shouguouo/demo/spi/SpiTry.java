package com.shouguouo.demo.spi;

import java.util.ServiceLoader;

/**
 * @author shouguouo
 * @date 2021-06-26 22:22:35
 */
public class SpiTry {

    public static void main(String[] args) {
        ServiceLoader<Hi> loadedHi = ServiceLoader.load(Hi.class);
        for (Hi impl : loadedHi) {
            impl.sayHi();
        }
    }

}
