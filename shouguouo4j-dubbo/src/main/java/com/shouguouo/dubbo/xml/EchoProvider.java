package com.shouguouo.dubbo.xml;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author shouguouo
 * @date 2021-12-25 10:28:25
 */
public class EchoProvider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring/echo-provider.xml" });
        context.start();
        System.out.println("EchoProvider STARTED");
        System.in.read();
    }
}
