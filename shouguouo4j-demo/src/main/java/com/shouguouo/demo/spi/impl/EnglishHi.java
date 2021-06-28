package com.shouguouo.demo.spi.impl;

import com.shouguouo.demo.spi.Hi;

/**
 * @author shouguouo
 * @date 2021-06-26 22:12:12
 */
public class EnglishHi implements Hi {

    @Override
    public void sayHi() {
        System.out.println("hello");
    }
}
