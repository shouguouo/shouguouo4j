package com.shouguouo.spring.aop.base;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author shouguouo
 * @date 2021-11-27 13:39:41
 */
@Component
public class JdkProxyBeanImpl implements JdkProxyBean {

    @Override
    public void say() {
        System.out.println("JdkProxyBeanImpl say");
        Object proxy = AopContext.currentProxy();
        // [interface com.shouguouo.spring.aop.base.JdkProxyBean, interface org.springframework.aop.SpringProxy, interface org.springframework.aop.framework.Advised, interface org.springframework.core.DecoratingProxy]
        System.out.println(Arrays.toString(proxy.getClass().getInterfaces()));
    }
}
