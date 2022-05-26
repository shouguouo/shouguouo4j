package com.shouguouo.dubbo.demo.sdk;

/**
 * @author shouguouo
 * @date 2022-05-26 15:55:55
 */
public interface GreetingService {

    String sayHello(String name);

    Result<String> testGeneric(PoJo poJo);
}
