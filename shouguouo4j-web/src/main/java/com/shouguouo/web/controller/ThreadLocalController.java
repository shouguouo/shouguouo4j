package com.shouguouo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shouguouo
 * @date 2021-09-07 16:38:41
 */
@RestController
@RequestMapping("threadLocal")
public class ThreadLocalController {

    private final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    /**
     * 存在的问题：tomcat线程复用导致before可能会获取到上个用户的信息
     *
     * @param userId 用户ID
     * @return 前后threadLocal中的值
     */
    @GetMapping("wrong")
    public Map<String, String> wrong(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        // 设置用户信息到ThreadLocal
        currentUser.set(userId);
        // 设置用户信息之后再查询一次ThreadLocal中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        // 汇总输出两次查询结果
        Map<String, String> result = new HashMap<>(2);
        result.put("before", before);
        result.put("after", after);
        return result;
    }

    /**
     * 正确写法 每次使用完threadLocal都要remove
     *
     * @param userId 用户ID
     * @return 前后threadLocal中的值
     */
    @GetMapping("right")
    public Map<String, String> right(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        // 设置用户信息到ThreadLocal
        currentUser.set(userId);
        try {
            // 设置用户信息之后再查询一次ThreadLocal中的用户信息
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            // 汇总输出两次查询结果
            Map<String, String> result = new HashMap<>(2);
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            currentUser.remove();
        }
    }

}
