package com.shouguouo.spring.aop.scope;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author shouguouo
 * @date 2021-11-28 13:28:43
 */
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("com.shouguouo.spring.aop.scope")
public class Configuration {

}
