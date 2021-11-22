package com.shouguouo.spring.beans;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shouguouo
 * @date 2021-11-22 16:06:45
 */
public class BeanFromAnnotationTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Teacher.class);
        Student student = context.getBean(Student.class);
        System.out.println(student);
    }
}
