package com.shouguouo.spring.aop.introduction;

import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-27 15:20:25
 */
@Component
public class DefaultRunInterfaceImpl implements RunInterface {

    @Override
    public void run() {
        System.out.println("DefaultRunInterfaceImpl running");
    }
}
