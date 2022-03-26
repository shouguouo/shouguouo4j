package com.shouguouo.demo.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shouguouo
 * @date 2022-03-26 22:13:40
 */
public class GatherByteBufferTry {

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile(
                GatherByteBufferTry.class.getResource("/").getPath() + File.separator + "gather.txt", "rw");
        FileChannel channel = file.getChannel();
        // 先清空文件内容
        channel.truncate(0);
        ByteBuffer header = ByteBuffer.allocateDirect(5);
        ByteBuffer body = ByteBuffer.allocateDirect(100);
        // 5字节
        header.put("HELLO".getBytes()).flip();
        // 11字节
        body.put("SHOUWEIJIAN".getBytes()).flip();
        long write = channel.write(new ByteBuffer[] { header, body });
        // 16字节
        System.out.println(write);
        System.out.println(channel.size());
        System.out.println(file.length());
    }
}
