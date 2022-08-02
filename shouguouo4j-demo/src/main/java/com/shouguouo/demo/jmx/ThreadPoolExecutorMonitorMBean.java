package com.shouguouo.demo.jmx;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shouguouo
 * @date 2022-08-02 11:47:05
 */
public interface ThreadPoolExecutorMonitorMBean {

    /**
     * 核心线程数
     *
     * @return 核心线程数
     */
    int getCorePoolSize();

    /**
     * 设置核心线程数
     */
    void setCorePoolSize(int size);

    /**
     * 当前等待队列长度
     *
     * @return 当前等待队列长度
     */
    int getPendingSize();

    /**
     * 获取队列剩余容量
     *
     * @return 获取队列剩余容量
     */
    int getQueueRemainingCapacity();

    /**
     * 获取队列类型
     *
     * @return 获取队列类型
     */
    String getQueueType();

    /**
     * 最大线程数
     *
     * @return 最大线程数
     */
    int getMaximumPoolSize();

    /**
     * 当前活跃度
     *
     * @return 当前活跃度
     */
    int getActiveCount();

    /**
     * 曾经最大线程数量
     *
     * @return 曾经最大线程数量
     */
    int getLargestPoolSize();

    /**
     * 当前线程数量
     *
     * @return 当前线程数量
     */
    int getPoolSize();

    /**
     * 线程最大存活时间
     *
     * @return 线程最大存活时间
     */
    long getKeepAliveTime();

    /**
     * 已完成任务数
     *
     * @return 已完成任务数
     */
    long getCompletedTaskCount();

    /**
     * 获取线程池对象
     *
     * @return 获取线程池对象
     */
    ThreadPoolExecutor getThreadPoolExecutor();

    /**
     * 打印
     *
     * @param str 字符串
     */
    void print(String str);
}
