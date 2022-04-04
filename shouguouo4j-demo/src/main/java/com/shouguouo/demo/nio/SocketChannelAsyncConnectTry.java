package com.shouguouo.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * port要有程序监听，可以与ServerSocketChannelTry搭配测试
 *
 * @author shouguouo
 * @date 2022-03-31 21:56:40
 */
public class SocketChannelAsyncConnectTry {

    public static void main(String[] argv) throws IOException, InterruptedException {
        String host = "127.0.0.1";
        int port = 1234;
        if (argv.length == 2) {
            host = argv[0];
            port = Integer.parseInt(argv[1]);
        }
        SocketChannel sc = SocketChannel.open();
        // 异步连接
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress(host, port));
        System.out.println("initiating connection");
        while (!sc.finishConnect()) {
            doSomethingUseful();
        }
        System.out.println("connection established");
        // Do something with the connected socket
        // The SocketChannel is still nonblocking
        System.out.println("isConnected: " + sc.isConnected());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10);
        WritableByteChannel out = Channels.newChannel(System.out);
        while (sc.read(byteBuffer) == 0) {
            // 等待数据读入
        }
        do {
            // 接收到所有数据
            byteBuffer.flip();
            out.write(byteBuffer);
            byteBuffer.clear();
        } while (sc.read(byteBuffer) > 0);
        sc.close();
    }

    private static void doSomethingUseful() {
        System.out.println("doing something useless");
    }
}