package com.shouguouo.spring.aop.full;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author shouguouo
 * @date 2021-11-28 00:23:23
 */
@Aspect(
        /*
        default singleton if prototype using the per clause expression
        example: perthis(execution(* com.dalianpai.service.impl.*.*(..)))
        */)
// @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class DoAspect {

    @Pointcut(value = "within(Do) && args(name, date)", argNames = "name, date")
    public void pointcut(String name, LocalDate date) {
    }

    @Before(value = "pointcut(name, date)", argNames = "name,date")
    public void doBefore(String name, LocalDate date) {
        System.out.println("before date: " + date);
    }

    @After(value = "pointcut(name,date)", argNames = "name,date")
    public void doAfter(String name, LocalDate date) {
        System.out.println("after name: " + name);
    }

    @AfterReturning(value = "within(Do) && args(name, date) && this(bean)", returning = "rt", argNames = "rt,name,date,bean")
    public void doAfterReturning(String rt, String name, LocalDate date, Object bean) {
        System.out.println("after returning args：" + name + "," + date + ",this：" + bean + ", result：" + rt);
    }

    @AfterThrowing(value = "within(Do) && args(name, date) && this(bean)", throwing = "ex", argNames = "ex,name,date,bean")
    public void doAfterThrowing(Throwable ex, String name, LocalDate date, Object bean) {
        System.out.println("after throwing args：" + name + "," + date + ",this：" + bean + ",result：" + ex.getMessage());
    }

    @Around(value = "pointcut(name, date)", argNames = "joinPoint,name,date")
    public Object around(ProceedingJoinPoint joinPoint, String name, LocalDate date) {
        System.out.println("around before：" + name + "," + date);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            return "around exceptionally：" + throwable.getMessage();
        }
        System.out.println("around after：" + proceed);
        return proceed;
    }
}
