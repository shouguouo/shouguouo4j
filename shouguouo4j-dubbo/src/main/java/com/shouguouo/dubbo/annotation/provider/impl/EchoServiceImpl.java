package com.shouguouo.dubbo.annotation.provider.impl;

import com.shouguouo.dubbo.annotation.service.EchoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author shouguouo
 * @date 2021-12-25 10:19:47
 */
@DubboService
@Service
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + now + "] Hello " + message + ", request from consumer: " + RpcContext.getServiceContext().getRemoteAddressString());
        return "Hello " + message + ", response from provider: " + RpcContext.getServiceContext().getLocalAddress();
    }
}
