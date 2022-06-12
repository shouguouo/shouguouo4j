package com.shouguouo.dubbo.demo.consumer;

import com.shouguouo.common.util.OutputUtils;
import com.shouguouo.dubbo.demo.sdk.GreetingService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import java.io.IOException;

/**
 * @author shouguouo
 * @date 2022-05-26 16:22:59
 */
public class ApiConsumer {

    public static void main(String[] args) {
        ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-consumer"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(GreetingService.class);
        referenceConfig.setTimeout(1);

        referenceConfig.setGroup("shouguouo");
        referenceConfig.setVersion("1.0.0");

        GreetingService greetingService = referenceConfig.get();

        RpcContext.getClientAttachment().setAttachment("company", "shouguouo");

        try {
            OutputUtils.printlnWithCurrentThread(greetingService.sayHello("world"));
        } catch (Throwable t) {
            OutputUtils.printStackTraceWithCurrentThread(t);
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
