package com.shouguouo.demo.nio.reactor;

import com.shouguouo.common.util.OutputUtils;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负责连接线程
 *
 * @author shouguouo
 * @date 2022-05-03 12:19:24
 */
public class MainReactor extends Thread {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final Selector selector;

    private final SubReactorGroup subReactorGroup;

    public MainReactor(ServerSocketChannel ssc, SubReactorGroup subReactorGroup) {
        this.subReactorGroup = subReactorGroup;
        try {
            this.selector = Selector.open();
            ssc.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setName("MainReactor-" + POOL_NUMBER.getAndIncrement());
    }

    @Override
    public void run() {
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
                OutputUtils.printlnWithCurrentThread("MainReactor Event " + selectionKey);
                iterator.remove();
                try {
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel accept = channel.accept();
                        accept.configureBlocking(false);
                        // 连接建立后交给IO线程
                        subReactorGroup.dispatch(accept);
                    }
                } catch (IOException e) {
                    try {
                        selectionKey.channel().close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
    }
}
