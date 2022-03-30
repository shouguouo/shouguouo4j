package com.shouguouo.demo.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shouguouo
 * @date 2022-03-26 21:36:56
 */
public class ScatterByteBufferTry {

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile(
                ScatterByteBufferTry.class.getResource("/a.txt").getPath(), "r");
        FileChannel channel = file.getChannel();
        ByteBuffer header = ByteBuffer.allocateDirect(5);
        ByteBuffer body = ByteBuffer.allocateDirect(100);
        // 讲文件内容读取到多个缓冲区中
        long read = channel.read(new ByteBuffer[] { header, body });
        System.out.println(read);
        byte[] hello = new byte[5];
        header.flip();
        header.get(hello);
        System.out.println(new String(hello));
        byte[] swj = new byte[11];
        body.flip();
        body.get(swj);
        System.out.println(new String(swj));
    }
}
