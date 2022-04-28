package com.shouguouo.netty.rpc;

import com.shouguouo.common.util.OutputUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author shouguouo
 * @date 2022-04-25 09:25:20
 */
public class AsyncRpcTry {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        RpcClient client = new RpcClient();
        System.out.println(client.rpcSyncCall("UNKNOW!"));
        OutputUtils.cuttingLine("Sync Call Over");
        CompletableFuture<String> task1 = client.rpcAsyncCall("ShouGuouo!");
        CompletableFuture<String> task2 = client.rpcAsyncCall("Rice!");
        CompletableFuture<String> task3 = task1.thenCombine(task2, (x, y) -> x + y);
        task3.whenComplete((x, t) -> {
            if (t != null) {
                t.printStackTrace();
            } else {
                System.out.println(x);
                client.close();
            }
        });
        OutputUtils.cuttingLine("Async Call Over");
    }

}
