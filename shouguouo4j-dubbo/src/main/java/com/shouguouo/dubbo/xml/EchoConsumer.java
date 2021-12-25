package com.shouguouo.dubbo.xml;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author shouguouo
 * @date 2021-12-25 11:10:23
 */
public class EchoConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring/echo-consumer.xml" });
        context.start();
        System.out.println("EchoConsumer STARTED");
        EchoService echoService = context.getBean(EchoService.class);
        String result = echoService.echo("shouguouo");
        System.out.println("echo result: " + result);
    }
}
