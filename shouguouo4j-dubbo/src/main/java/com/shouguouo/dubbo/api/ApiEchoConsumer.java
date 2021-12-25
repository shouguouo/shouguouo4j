package com.shouguouo.dubbo.api;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.io.IOException;

/**
 * @author shouguouo
 * @date 2021-12-25 21:59:25
 */
public class ApiEchoConsumer {

    public static void main(String[] args) throws IOException {
        ReferenceConfig<EchoService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("api-echo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        reference.setInterface(EchoService.class);
        EchoService echoService = reference.get();
        String result = echoService.echo("shouguouo");
        System.out.println(result);
        System.in.read();
    }
}
