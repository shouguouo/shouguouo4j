package com.shouguouo.demo.nio.reactor;

import com.shouguouo.common.util.OutputUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 业务处理
 *
 * @author shouguouo
 * @date 2022-05-03 14:19:50
 */
public class BusinessHandler implements Runnable {

    private static final byte[] FOOTER = "hello,服务器收到了你的信息。\r\n".getBytes();

    private final SocketChannel channel;

    private final ByteBuffer byteBuffer;

    private final SubReactor subReactor;

    public BusinessHandler(SocketChannel channel, ByteBuffer byteBuffer, SubReactor subReactor) {
        this.channel = channel;
        this.byteBuffer = byteBuffer;
        this.subReactor = subReactor;
    }

    @Override
    public void run() {
        OutputUtils.printlnWithCurrentThread("BusinessHandler Started");
        byteBuffer.put(FOOTER);
        // 业务线程处理完毕，返回IO线程
        subReactor.offer(new Task(channel, SelectionKey.OP_WRITE, byteBuffer));
        OutputUtils.printlnWithCurrentThread("BusinessHandler End");
    }
}
