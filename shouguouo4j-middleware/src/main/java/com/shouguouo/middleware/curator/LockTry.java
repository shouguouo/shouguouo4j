package com.shouguouo.middleware.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2021-10-11 15:32:41
 */
public class LockTry {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new RetryNTimes(10, 5000));
        client.start();
        try {
            InterProcessMutex interProcessMutex = new InterProcessMutex(client, "/test");
            interProcessMutex.acquire();
            System.out.println("acquired");
            TimeUnit.SECONDS.sleep(30);
            interProcessMutex.release();
            System.out.println("released");
        } finally {
            client.close();
        }
    }
}
