package com.shouguouo.spring.propertyeditor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shouguouo
 * @date 2021-11-25 15:30:47
 */
public class PropertyEditorTry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        Boss boss = context.getBean(Boss.class);
        System.out.println(boss);
    }
}
