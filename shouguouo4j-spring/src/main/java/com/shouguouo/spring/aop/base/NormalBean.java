package com.shouguouo.spring.aop.base;

import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-27 12:47:14
 */
@Component
public class NormalBean {

    public void say() {
        System.out.println("NormalBean say");
    }
}
