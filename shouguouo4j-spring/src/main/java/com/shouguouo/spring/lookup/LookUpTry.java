package com.shouguouo.spring.lookup;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shouguouo
 * @date 2021-11-25 16:49:55
 */
public class LookUpTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        Shou shou = context.getBean(Shou.class);
        Xiang xiang = shou.getXiang();
        System.out.println(shou);
        System.out.println(xiang);
    }
}
