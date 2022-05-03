package com.shouguouo.demo.nio.reactor;

import com.shouguouo.common.util.OutputUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IO线程
 *
 * @author shouguouo
 * @date 2022-05-03 12:19:32
 */
public class SubReactor extends Thread {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    /**
     * 每个IO线程共用一个Selector
     */
    private final Selector selector;

    /**
     * 业务线程池
     */
    private final ExecutorService businessExecutor;

    private final LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public SubReactor(ExecutorService businessExecutor) {
        this.businessExecutor = businessExecutor;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setName("SubReactor-" + POOL_NUMBER.getAndIncrement());
    }

    public void offer(Task task) {
        taskQueue.offer(task);
    }

    @Override
    public void run() {
        // 支持中断
        while (!Thread.interrupted()) {
            Set<SelectionKey> selectionKeys;
            try {
                selector.select(1);
                selectionKeys = selector.selectedKeys();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            // 处理事件
            for (Iterator<SelectionKey> iterator = selectionKeys.iterator(); iterator.hasNext(); ) {
                SelectionKey selectionKey = iterator.next();
                OutputUtils.printlnWithCurrentThread("SubReactor Event " + selectionKey);
                iterator.remove();
                try {
                    if (selectionKey.isWritable()) {
                        // 可写，向客户端写请求
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        // 携带的对象
                        ByteBuffer data = (ByteBuffer) selectionKey.attachment();
                        data.flip();
                        channel.write(data);
                        // 注册客户端读事件
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                    if (selectionKey.isReadable()) {
                        // 可读，读取客户端请求
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);
                        // 业务线程处理请求
                        businessExecutor.submit(new BusinessHandler(channel, byteBuffer, this));
                    }
                } catch (IOException e) {
                    try {
                        selectionKey.channel().close();
                    } catch (IOException ee) {
                        e.printStackTrace();
                        ee.printStackTrace();
                    }
                }
            }
            // 处理任务
            for (int i = taskQueue.size() - 1; i >= 0; i--) {
                Task task = taskQueue.poll();
                if (task == null) {
                    continue;
                }
                OutputUtils.printlnWithCurrentThread("SubReactor Task " + task);
                SocketChannel sc = task.getSc();
                try {
                    sc.register(selector, task.getOp(), task.getData());
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
