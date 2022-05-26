package com.shouguouo.dubbo.demo.provider;

import com.alibaba.fastjson.JSON;
import com.shouguouo.dubbo.demo.sdk.GreetingService;
import com.shouguouo.dubbo.demo.sdk.PoJo;
import com.shouguouo.dubbo.demo.sdk.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2022-05-26 16:09:18
 */
@Slf4j
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String sayHello(String name) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello " + name + " " + RpcContext.getServerAttachment().getAttachment("company");
    }

    @Override
    public Result<String> testGeneric(PoJo poJo) {
        Result<String> result = new Result<>();
        result.setSuccess(true);
        result.setData(JSON.toJSONString(poJo));
        return result;
    }
}
