package com.shouguouo.spring.spi;

import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author shouguouo
 * @date 2021-06-29 11:25:33
 */
public class SpiTry {

    public static void main(String[] args) {
        List<Hi> loaded = SpringFactoriesLoader.loadFactories(Hi.class, null);
        for (Hi hi : loaded) {
            hi.sayHi();
        }
    }

}
