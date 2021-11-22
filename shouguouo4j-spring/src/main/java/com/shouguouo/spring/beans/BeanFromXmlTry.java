package com.shouguouo.spring.beans;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author shouguouo
 * @date 2021-11-22 16:00:05
 */
public class BeanFromXmlTry {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/beans.xml");
        Student student = applicationContext.getBean(Student.class);
        System.out.println(student);
    }
}
