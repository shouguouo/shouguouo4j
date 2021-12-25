package com.shouguouo.dubbo.annotation.consumer;

import com.shouguouo.dubbo.annotation.service.EchoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-12-25 13:56:34
 */
@Component
public class EchoConsumer {

    @DubboReference
    private EchoService echoService;

    public String echo(String name) {
        return echoService.echo(name);
    }
}
