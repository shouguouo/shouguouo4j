package com.shouguouo.spring.aop.introduction;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-27 15:21:50
 */
@Aspect
@Component
public class ProxyAspect {

    @DeclareParents(value = "com.shouguouo.spring.aop.introduction.Person", defaultImpl = DefaultRunInterfaceImpl.class)
    @SuppressWarnings("unused")
    private RunInterface runInterface;
}
