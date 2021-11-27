package com.shouguouo.spring.aop.introduction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * @author shouguouo
 * @date 2021-11-27 15:29:46
 */
public class IntroductionTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        RunInterface runInterface = context.getBean("person", RunInterface.class);
        runInterface.run();
        System.out.println(runInterface.getClass());
        System.out.println(Arrays.toString(runInterface.getClass().getInterfaces()));
    }
}
