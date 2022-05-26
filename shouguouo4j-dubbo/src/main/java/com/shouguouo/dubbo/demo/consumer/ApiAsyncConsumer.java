package com.shouguouo.dubbo.demo.consumer;

import com.shouguouo.common.util.OutputUtils;
import com.shouguouo.dubbo.demo.sdk.GreetingServiceAsync;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author shouguouo
 * @date 2022-05-26 16:39:22
 */
public class ApiAsyncConsumer {

    public static void main(String[] args) {
        ReferenceConfig<GreetingServiceAsync> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-consumer"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(GreetingServiceAsync.class);
        referenceConfig.setTimeout(3000);

        referenceConfig.setGroup("shouguouo");
        referenceConfig.setVersion("1.0.0");

        referenceConfig.setAsync(true);

        GreetingServiceAsync greetingServiceAsync = referenceConfig.get();
        RpcContext.getClientAttachment().setAttachment("company", "shouguouo");

        greetingServiceAsync.sayHello("world").whenComplete((result, t) -> {
            OutputUtils.printlnWithCurrentThread(result);
        });
    }
}
