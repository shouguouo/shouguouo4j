package com.shouguouo.dubbo.annotation.provider;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author shouguouo
 * @date 2021-12-25 11:16:37
 */
public class AnnotationProvider {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        context.start();
        System.out.println("EchoProvider STARTED");
        System.in.read();
    }

    @org.springframework.context.annotation.Configuration
    @EnableDubbo(scanBasePackages = "com.shouguouo.dubbo.annotation.provider")
    static class Configuration {

        @Bean
        public ApplicationConfig applicationConfig() {
            return new ApplicationConfig("echo-annotation-provider");
        }

        @Bean
        public RegistryConfig registryConfig() {
            RegistryConfig registryConfig = new RegistryConfig("127.0.0.1", "zookeeper");
            registryConfig.setPort(2181);
            return registryConfig;
        }

        @Bean
        public ProtocolConfig protocolConfig() {
            return new ProtocolConfig("dubbo", 20880);
        }

        @Bean
        public ProviderConfig providerConfig() {
            return new ProviderConfig();
        }
    }

}
