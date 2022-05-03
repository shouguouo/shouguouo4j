package com.shouguouo.demo.nio.reactor;

import com.shouguouo.common.util.OutputUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负责建立连接
 *
 * @author shouguouo
 * @date 2022-05-03 13:00:55
 */
public class Acceptor extends Thread {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final InetSocketAddress address;

    private final MainReactorGroup mainReactorGroup;

    public Acceptor(InetSocketAddress address, MainReactorGroup mainReactorGroup, SubReactorGroup subReactorGroup) {
        this.address = address;
        this.mainReactorGroup = mainReactorGroup;
        // 建立主从关系
        this.mainReactorGroup.related(subReactorGroup);
        this.setName("Accepter " + POOL_NUMBER.getAndIncrement());
    }

    @Override
    public void run() {
        try {
            OutputUtils.printlnWithCurrentThread("Acceptor Started");
            // 建立连接
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(address).configureBlocking(false);
            // 分发
            mainReactorGroup.initAndStart(ssc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputUtils.printlnWithCurrentThread("Acceptor End");
    }
}
