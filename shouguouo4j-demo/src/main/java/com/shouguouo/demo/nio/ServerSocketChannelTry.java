package com.shouguouo.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * 启动该类，使用"telnet localhost port"来连接
 *
 * @author shouguouo
 * @date 2022-03-30 22:19:56
 */
public class ServerSocketChannelTry {

    public static final String GREETING = "Hello I must be going.\r\n";

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 1234;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        // 非阻塞
        ssc.configureBlocking(false);
        while (true) {
            System.out.println("Waiting for connections");
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                TimeUnit.SECONDS.sleep(2);
            } else {
                System.out.println("Incoming connection from: "
                        + sc.getRemoteAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }

}
