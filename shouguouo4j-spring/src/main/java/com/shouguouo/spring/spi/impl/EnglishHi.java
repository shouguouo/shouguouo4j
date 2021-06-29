package com.shouguouo.spring.spi.impl;

import com.shouguouo.spring.spi.Hi;
import org.springframework.core.annotation.Order;

/**
 * @author shouguouo
 * @date 2021-06-29 11:24:37
 */
@Order(1)
public class EnglishHi implements Hi {

    @Override
    public void sayHi() {
        System.out.println("hello");
    }
}
