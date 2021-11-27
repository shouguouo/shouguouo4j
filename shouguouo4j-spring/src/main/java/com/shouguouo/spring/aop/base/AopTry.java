package com.shouguouo.spring.aop.base;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shouguouo
 * @date 2021-11-27 12:55:24
 */
public class AopTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        NormalBean normalBean = context.getBean(NormalBean.class);
        normalBean.say();
        System.out.println(AopUtils.getTargetClass(normalBean));
        System.out.println("--------------------------------------------------------");
        ProxyBean proxyBean = context.getBean(ProxyBean.class);
        proxyBean.say();
        System.out.println(AopUtils.getTargetClass(proxyBean));
        System.out.println("--------------------------------------------------------");
        JdkProxyBean jdkProxyBean = context.getBean(JdkProxyBean.class);
        jdkProxyBean.say();
        System.out.println(AopUtils.getTargetClass(jdkProxyBean));
    }

}
