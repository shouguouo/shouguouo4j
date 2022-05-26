package com.shouguouo.dubbo.demo.sdk;

import java.util.concurrent.CompletableFuture;

/**
 * @author shouguouo
 * @date 2022-05-26 16:00:22
 */
public interface GreetingServiceAsync {

    CompletableFuture<String> sayHello(String name);

}
