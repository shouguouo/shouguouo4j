package com.shouguouo.dubbo.api;

import com.shouguouo.dubbo.api.impl.EchoServiceImpl;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.io.IOException;

/**
 * @author shouguouo
 * @date 2021-12-25 21:53:13
 */
public class ApiEchoProvider {

    public static void main(String[] args) throws IOException {
        ServiceConfig<EchoService> serviceProvider = new ServiceConfig<>();
        serviceProvider.setApplication(new ApplicationConfig("api-echo-provider"));
        serviceProvider.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        serviceProvider.setInterface(EchoService.class);
        serviceProvider.setRef(new EchoServiceImpl());
        serviceProvider.export();
        System.out.println("api-echo-provider started");
        System.in.read();
    }
}
