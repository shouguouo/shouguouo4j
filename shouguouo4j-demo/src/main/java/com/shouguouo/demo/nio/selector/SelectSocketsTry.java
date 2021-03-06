package com.shouguouo.demo.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;

/**
 * 利用选择器实现一个线程管理多个Socket连接，接收数据并回复OK！
 *
 * @author shouguouo
 * @date 2022-04-05 13:11:23
 */
public class SelectSocketsTry {

    public static int PORT_NUMBER = 1234;

    private final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    protected final WritableByteChannel out = Channels.newChannel(System.out);

    public static void main(String[] args) throws IOException {
        new SelectSocketsTry().go(args);
    }

    public void go(String[] args) throws IOException {
        int port = PORT_NUMBER;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        System.out.println("Listening on port " + port);

        // 选择器
        Selector selector = Selector.open();
        // 通道
        SelectableChannel selectableChannel = ServerSocketChannel
                .open()
                .bind(new InetSocketAddress(port))
                .configureBlocking(false);
        // 维护选择器和通道的关系
        selectableChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 阻塞
            int n = selector.select();
            if (n == 0) {
                continue;
            }
            // 管理已就绪的通道
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) next.channel();
                    SocketChannel channel = server.accept();
                    // 管理指定端口传输数据通道的可读状态
                    registerChannel(selector, channel, SelectionKey.OP_READ);
                    // 向该通道发送已建立连接的回复
                    write(channel, "Hi there!\r\n");
                }
                if (next.isReadable()) {
                    // 已建立通道可读
                    readFromSocket(next);
                }
                // 已处理过的key从就绪列表中移除
                iterator.remove();
            }
        }
    }

    private void registerChannel(Selector selector, SelectableChannel channel, int ops) throws IOException {
        if (channel == null) {
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector, ops);
    }

    protected void readFromSocket(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        write(out, Thread.currentThread().getName() + ": Read From " + channel.getRemoteAddress() + ": ");
        int count;
        buffer.clear(); // Empty buffer
        // Loop while data is available; channel is nonblocking
        while ((count = channel.read(buffer)) > 0) {
            buffer.flip(); // make buffer readable
            // Send the data; may not go all at once
            while (buffer.hasRemaining()) {
                out.write(buffer);
            }
            buffer.clear(); // Empty buffer
        }
        // 客户端关闭也会受到读就绪状态的通知，count为-1
        if (count < 0) {
            write(out, "closed\r\n");
            // Close channel on EOF; invalidates the key
            channel.close();
            return;
        }
        write(channel, "OK!\r\n");
    }

    public void write(WritableByteChannel channel, String message) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        channel.write(buffer);
    }
}
