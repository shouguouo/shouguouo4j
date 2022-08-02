package com.shouguouo.demo.jmx;

import com.shouguouo.common.util.OutputUtils;
import lombok.NonNull;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shouguouo
 * @date 2022-08-02 11:50:54
 */
public class MonitorTry {

    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
            4,
            8,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactory() {
                private static final String NAME = "shouguouo-monitor-thread-pool-%s";

                private final AtomicInteger poolNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName(String.format(NAME, poolNumber.getAndIncrement()));
                    return thread;
                }
            });

    static {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            // domain:key-property-list
            ObjectName objectName = new ObjectName("com.shouguouo.demo.jmx:type=Thread,name=MonitorTry");
            if (!mBeanServer.isRegistered(objectName)) {
                ThreadPoolExecutorMonitor mBean = new ThreadPoolExecutorMonitor(POOL);
                mBeanServer.registerMBean(mBean, objectName);
            }
        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            final String s = "" + i;
            POOL.execute(() -> OutputUtils.printlnWithCurrentThread(s));
        }
        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();
    }
}
