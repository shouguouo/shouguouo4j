package com.shouguouo.dubbo.demo.provider;

import com.shouguouo.common.util.OutputUtils;
import com.shouguouo.dubbo.demo.sdk.GreetingService;
import com.shouguouo.dubbo.demo.sdk.GreetingServiceAsync;
import com.shouguouo.dubbo.demo.sdk.GreetingServiceRpcContext;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.io.IOException;

/**
 * @author shouguouo
 * @date 2022-05-26 16:17:17
 */
public class ApiProvider {

    public static void main(String[] args) throws IOException {
        start();
        startAsync();
        startAsyncWithContext();
        OutputUtils.cuttingLine("started");
        System.in.read();
    }

    private static void start() {
        ServiceConfig<GreetingService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-provider"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setInterface(GreetingService.class);
        serviceConfig.setRef(new GreetingServiceImpl());

        serviceConfig.setGroup("shouguouo");
        serviceConfig.setVersion("1.0.0");

        serviceConfig.export();
    }

    private static void startAsync() {
        ServiceConfig<GreetingServiceAsync> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-provider"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setInterface(GreetingServiceAsync.class);
        serviceConfig.setRef(new GreetingServiceAsyncImpl());

        serviceConfig.setGroup("shouguouo");
        serviceConfig.setVersion("1.0.0");

        serviceConfig.setAsync(true);

        serviceConfig.export();
    }

    private static void startAsyncWithContext() {
        ServiceConfig<GreetingServiceRpcContext> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-provider"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setInterface(GreetingServiceRpcContext.class);
        serviceConfig.setRef(new GreetingServiceRpcContextImpl());

        serviceConfig.setGroup("shouguouo");
        serviceConfig.setVersion("1.0.0");

        serviceConfig.setAsync(true);

        serviceConfig.export();
    }
}
