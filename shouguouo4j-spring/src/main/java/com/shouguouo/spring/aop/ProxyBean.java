package com.shouguouo.spring.aop;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author shouguouo
 * @date 2021-11-27 12:46:55
 */
@Component
public class ProxyBean {

    public void say() {
        System.out.println("ProxyBean say");
        Object proxy = AopContext.currentProxy();
        // Cglib [interface org.springframework.aop.SpringProxy, interface org.springframework.aop.framework.Advised, interface org.springframework.cglib.proxy.Factory]
        System.out.println(Arrays.toString(proxy.getClass().getInterfaces()));
    }
}
