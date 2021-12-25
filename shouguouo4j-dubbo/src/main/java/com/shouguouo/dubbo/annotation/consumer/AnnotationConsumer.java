package com.shouguouo.dubbo.annotation.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shouguouo
 * @date 2021-12-25 13:10:14
 */
public class AnnotationConsumer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotationConsumer.Configuration.class);
        context.start();
        System.out.println("EchoConsumer STARTED");
        EchoConsumer echoService = context.getBean(EchoConsumer.class);
        String result = echoService.echo("shouguouo");
        System.out.println("echo result: " + result);
    }

    @org.springframework.context.annotation.Configuration
    @EnableDubbo(scanBasePackages = "com.shouguouo.dubbo.annotation.consumer")
    @ComponentScan(value = { "com.shouguouo.dubbo.annotation.consumer" })
    static class Configuration {

        @Bean
        public ApplicationConfig applicationConfig() {
            return new ApplicationConfig("echo-annotation-consumer");
        }

        @Bean
        public RegistryConfig registryConfig() {
            RegistryConfig registryConfig = new RegistryConfig("127.0.0.1", "zookeeper");
            registryConfig.setPort(2181);
            return registryConfig;
        }

        @Bean
        public ConsumerConfig consumerConfig() {
            return new ConsumerConfig();
        }
    }

}
