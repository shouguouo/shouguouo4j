package com.shouguouo.dubbo.demo.consumer;

import lombok.experimental.UtilityClass;

/**
 * @author shouguouo
 * @date 2022-05-26 20:30:13
 */
@UtilityClass
public class Hotchpotch {

    /**
     * Mock
     *
     * 本地服务Mock，Mock实现类为接口包名.类名Mock
     * 消费服务配置时，mock属性设置为true，check属性设置为false，不检查服务端是否可用
     * 当远程调用失败时，才降级执行Mock功能
     */

    /**
     * 降级
     *
     * force + return: 强制返回return配置的Mock值
     * fail + return: 只有失败时才返回Mock值
     * 通过URI实现降级配置
     */

    /**
     * 隐式参数传递
     * RpcContext.getContext().setAttachment
     * RpcContext.getContext().getAttachment
     * 以及对应的Client端和Server端Context
     */

    /**
     * 本地服务暴露和引用
     * injvm://
     *
     * 默认行为，如果本地JVM中存在对应服务的引用
     * 可指定scope: remote/local
     */
}
