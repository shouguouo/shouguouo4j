package com.shouguouo.spring.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shouguouo
 * @date 2021-11-22 16:04:57
 */
@Configuration
public class Teacher {

    @Bean
    public Student student() {
        Student student = new Student();
        student.setName("xhy");
        student.setAge(25);
        return student;
    }
}
