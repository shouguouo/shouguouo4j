package com.shouguouo.spring.circulation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shouguouo
 * @date 2021-11-25 09:54:14
 */
public class CirculationTry {

    public static void main(String[] args) {
        // setter注入
        System.out.println("--------------setter注入------------");
        setterInject();
        // 禁用循环引用
        System.out.println("--------------禁用循环引用------------");
        try {
            disableCirculation();
        } catch (Exception bci) {
            System.err.println(bci.getMessage());
        }
        // 非单例
        System.out.println("--------------非单例------------");
        try {
            prototype();
        } catch (Exception bci) {
            System.err.println(bci.getMessage());
        }
        // 构造器注入
        System.out.println("--------------构造器注入------------");
        try {
            constructInject();
        } catch (Exception bci) {
            System.err.println(bci.getMessage());
        }
        // 延迟加载
        System.out.println("--------------延迟加载------------");
        lazyInit();
    }

    public static void setterInject() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        First first = context.getBean(First.class);
        System.out.println(first + " autowired: " + first.getSecond());
        Second second = context.getBean(Second.class);
        System.out.println(second + " autowired: " + second.getFirst());
        context.close();
    }

    public static void constructInject() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationConstruct.class);
        FirstConstruct first = context.getBean(FirstConstruct.class);
        System.out.println(first + " autowired: " + first.getSecondConstruct());
        SecondConstruct second = context.getBean(SecondConstruct.class);
        System.out.println(second + " autowired: " + second.getFirstConstruct());
        context.close();
    }

    public static void lazyInit() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationLazyInit.class);
        FirstLazyInit first = context.getBean(FirstLazyInit.class);
        System.out.println(first + " autowired: " + first.getSecondLazyInit());
        SecondLazyInit second = context.getBean(SecondLazyInit.class);
        System.out.println(second + " autowired: " + second.getFirstLazyInit());
        context.close();
    }

    public static void disableCirculation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setAllowCircularReferences(false);
        context.register(Configuration.class);
        context.refresh();
        First first = context.getBean(First.class);
        System.out.println(first + " autowired: " + first.getSecond());
        Second second = context.getBean(Second.class);
        System.out.println(second + " autowired: " + second.getFirst());
        context.close();
    }

    public static void prototype() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationPrototype.class);
        FirstPrototype first = context.getBean(FirstPrototype.class);
        System.out.println(first + " autowired: " + first.getSecondPrototype());
        SecondPrototype second = context.getBean(SecondPrototype.class);
        System.out.println(second + " autowired: " + second.getFirstPrototype());
        context.close();
    }
}
