package com.shouguouo.demo.jmx;

import com.shouguouo.common.util.OutputUtils;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2022-08-02 11:50:10
 */
public class ThreadPoolExecutorMonitor implements ThreadPoolExecutorMonitorMBean {

    private final ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolExecutorMonitor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public int getCorePoolSize() {
        return threadPoolExecutor.getCorePoolSize();
    }

    @Override
    public void setCorePoolSize(int size) {
        threadPoolExecutor.setCorePoolSize(size);
    }

    @Override
    public int getPendingSize() {
        return threadPoolExecutor.getQueue().size();
    }

    @Override
    public int getMaximumPoolSize() {
        return threadPoolExecutor.getMaximumPoolSize();
    }

    @Override
    public int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }

    @Override
    public int getLargestPoolSize() {
        return threadPoolExecutor.getLargestPoolSize();
    }

    @Override
    public int getPoolSize() {
        return threadPoolExecutor.getPoolSize();
    }

    @Override
    public long getKeepAliveTime() {
        return threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS);
    }

    @Override
    public long getCompletedTaskCount() {
        return threadPoolExecutor.getCompletedTaskCount();
    }

    @Override
    public int getQueueRemainingCapacity() {
        return threadPoolExecutor.getQueue().remainingCapacity();
    }

    @Override
    public String getQueueType() {
        return threadPoolExecutor.getQueue().getClass().getSimpleName();
    }

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    @Override
    public void print(String str) {
        threadPoolExecutor.execute(() -> OutputUtils.printlnWithCurrentThread(str));
    }
}
