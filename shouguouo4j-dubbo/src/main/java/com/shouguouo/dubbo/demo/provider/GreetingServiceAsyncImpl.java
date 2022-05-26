package com.shouguouo.dubbo.demo.provider;

import com.shouguouo.dubbo.demo.sdk.GreetingServiceAsync;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2022-05-26 16:33:54
 */
public class GreetingServiceAsyncImpl implements GreetingServiceAsync {

    @Override
    public CompletableFuture<String> sayHello(String name) {
        String company = RpcContext.getServerAttachment().getAttachment("company");
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello " + name + " " + company;
        });
    }
}
