package com.shouguouo.spring.aop.base;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author shouguouo
 * @date 2021-11-27 12:43:10
 */
@org.springframework.context.annotation.Configuration
@ComponentScan("com.shouguouo.spring.aop.base")
@EnableAspectJAutoProxy(
        /*接口使用JDK动态代理，类使用CgLib代理 true则统一使用CgLib*/ proxyTargetClass = false,
        /*将代理类暴露给外部，通过AopContext.currentProxy()获取，ThreadLocal实现*/ exposeProxy = true)
public class Configuration {

}
