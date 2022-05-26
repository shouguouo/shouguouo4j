package com.shouguouo.dubbo.demo.consumer;

import com.shouguouo.common.util.OutputUtils;
import com.shouguouo.dubbo.demo.sdk.GreetingServiceRpcContext;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author shouguouo
 * @date 2022-05-26 19:09:14
 */
public class ApiAsyncWithContextConsumer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ReferenceConfig<GreetingServiceRpcContext> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("shouguouo-dubbo-consumer"));

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(GreetingServiceRpcContext.class);
        referenceConfig.setTimeout(3000);

        referenceConfig.setGroup("shouguouo");
        referenceConfig.setVersion("1.0.0");

        referenceConfig.setAsync(true);

        GreetingServiceRpcContext greetingServiceRpcContext = referenceConfig.get();
        RpcContext.getClientAttachment().setAttachment("company", "shouguouo");

        greetingServiceRpcContext.sayHello("world");
        ((CompletableFuture<String>) (RpcContext.getClientAttachment().<String>getFuture())).whenComplete((result, t) -> {
            OutputUtils.printlnWithCurrentThread(result);
        });
    }
}
