package com.shouguouo.netty.rpc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shouguouo
 * @date 2022-04-24 22:36:21
 */
public class FutureMapUtils {

    private static final ConcurrentHashMap<String, CompletableFuture<String>> FUTURE_MAP = new ConcurrentHashMap<>();

    public static void put(String id, CompletableFuture<String> future) {
        FUTURE_MAP.put(id, future);
    }

    public static CompletableFuture<String> remove(String id) {
        return FUTURE_MAP.remove(id);
    }
}
