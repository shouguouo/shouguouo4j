package com.shouguouo.spring.aop.full;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author shouguouo
 * @date 2021-11-28 00:20:44
 */
@org.springframework.context.annotation.Configuration
@ComponentScan("com.shouguouo.spring.aop.full")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Configuration {

}
