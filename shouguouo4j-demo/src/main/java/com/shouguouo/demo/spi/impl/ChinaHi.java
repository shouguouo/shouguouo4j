package com.shouguouo.demo.spi.impl;

import com.shouguouo.demo.spi.Hi;

/**
 * @author shouguouo
 * @date 2021-06-26 22:11:19
 */
public class ChinaHi implements Hi {

    @Override
    public void sayHi() {
        System.out.println("你好");
    }
}
