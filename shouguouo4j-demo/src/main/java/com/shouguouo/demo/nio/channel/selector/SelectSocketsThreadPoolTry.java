package com.shouguouo.demo.nio.channel.selector;

import com.shouguouo.common.util.OutputUtils;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用线程池处理单个端口上的多个客户端连接的事件
 *
 * @author shouguouo
 * @date 2022-04-05 20:38:32
 */
public class SelectSocketsThreadPoolTry extends SelectSocketsTry {

    private static final int MAX_THREADS = 5;

    private final ThreadPool pool = new ThreadPool(MAX_THREADS);

    public static void main(String[] args) throws IOException {
        new SelectSocketsThreadPoolTry().go(args);
    }

    @Override
    protected void readFromSocket(SelectionKey key) {
        WorkerThread workerThread = pool.getWorker();
        if (workerThread == null) {
            // 未获取到工作线程，直接返回 上层保证了会继续下次调用
            return;
        }
        workerThread.serviceChannel(key);
    }

    private class ThreadPool {

        final List<WorkerThread> idle = new LinkedList<>();

        ThreadPool(int poolSize) {
            // 初始化线程开启工作
            for (int i = 0; i < poolSize; i++) {
                WorkerThread workerThread = new WorkerThread(this);
                workerThread.setName("Worker " + (i + 1));
                workerThread.start();
                idle.add(workerThread);
            }
        }

        /**
         * 获取工作线程
         *
         * @return 工作线程
         */
        WorkerThread getWorker() {
            WorkerThread workerThread = null;
            synchronized (idle) {
                if (idle.size() > 0) {
                    workerThread = idle.remove(0);
                }
            }
            return workerThread;
        }

        /**
         * 工作线程使用完毕，归还
         *
         * @param workerThread 工作线程
         */
        void returnWorker(WorkerThread workerThread) {
            synchronized (idle) {
                idle.add(workerThread);
            }
        }
    }

    private class WorkerThread extends Thread {

        private final ThreadPool pool;

        private SelectionKey key;

        WorkerThread(ThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public synchronized void run() {
            OutputUtils.printlnWithCurrentThread("is ready");
            while (true) {
                try {
                    // Sleep and release object lock
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // Clear interrupt status
                    interrupted();
                }
                if (key == null) {
                    continue; // just in case
                }
                OutputUtils.printlnWithCurrentThread("has been awakened");
                try {
                    drainChannel(key);
                } catch (Exception e) {
                    OutputUtils.printStackTraceWithCurrentThread(e);
                    OutputUtils.printlnWithCurrentThread("Caught '" + e
                            + "' closing channel", OutputUtils.Level.ERROR);
                    // Close channel and nudge selector
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    key.selector().wakeup();
                }
                key = null;
                // Done. Ready for more. Return to pool
                this.pool.returnWorker(this);
            }
        }

        synchronized void serviceChannel(SelectionKey key) {
            this.key = key;
            // 任务执行期间不再监听读操作,防止上层循环时，多个线程操作单个通道
            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
            this.notify(); // Awaken the thread
        }

        void drainChannel(SelectionKey key) throws Exception {
            // 实际处理
            SelectSocketsThreadPoolTry.super.readFromSocket(key);
            // 恢复读操作的监听
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            // Cycle the selector so this key is active again
            key.selector().wakeup();
        }
    }
}
