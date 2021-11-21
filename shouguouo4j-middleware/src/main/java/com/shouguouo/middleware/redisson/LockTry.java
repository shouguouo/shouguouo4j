package com.shouguouo.middleware.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2021-10-11 11:31:08
 */
public class LockTry {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        RLock lock = redisson.getLock("anyLock");
        lock.lock();
        try {
            TimeUnit.SECONDS.sleep(120);
        } finally {
            lock.unlock();
        }
    }
}
