package com.shouguouo.spring.extend;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shouguouo
 * @date 2022-08-24 20:13:18
 */
public class ExtendTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        ExtendBean bean = context.getBean(ExtendBean.class);
    }
}
