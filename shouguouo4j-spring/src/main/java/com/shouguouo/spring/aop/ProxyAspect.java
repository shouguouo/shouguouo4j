package com.shouguouo.spring.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-27 12:47:33
 */
@Aspect
@Component
public class ProxyAspect {

    /**
     * @see <a href="https://cloud.tencent.com/developer/article/1455539">Spring AOP 中切入点 Pointcut中Expression表达式解析及配置</a>
     */
    @Pointcut("within(com.shouguouo.spring.aop.*ProxyBean*)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointCut()")
    public void after() {
        System.out.println("after");
    }
}
