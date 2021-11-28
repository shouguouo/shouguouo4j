package com.shouguouo.spring.aop.scope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shouguouo
 * @date 2021-11-28 13:31:37
 */
public class ScopeTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        SingletonBean singletonBean = context.getBean(SingletonBean.class);
        System.out.println(singletonBean.getPrototypeBean());
        System.out.println(singletonBean.getPrototypeBean());
    }
}
