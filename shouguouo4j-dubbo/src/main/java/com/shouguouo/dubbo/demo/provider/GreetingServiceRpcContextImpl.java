package com.shouguouo.dubbo.demo.provider;

import com.shouguouo.common.util.ExecutorUtils;
import com.shouguouo.dubbo.demo.sdk.GreetingServiceRpcContext;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2022-05-26 17:09:19
 */
public class GreetingServiceRpcContextImpl implements GreetingServiceRpcContext {

    @Override
    public String sayHello(String name) {
        final AsyncContext asyncContext = RpcContext.startAsync();
        String company = RpcContext.getServerAttachment().getAttachment("company");
        ExecutorUtils.execute(() -> {
            asyncContext.signalContextSwitch();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            asyncContext.write("Hello " + name + " " + company);
        });
        return null;
    }
}
