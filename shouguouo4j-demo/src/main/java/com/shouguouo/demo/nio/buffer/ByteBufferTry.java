package com.shouguouo.demo.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author shouguouo
 * @date 2022-01-01 00:39:43
 */
public class ByteBufferTry {

    public static void main(String[] args) {
        // byte[] buff 内部用于缓存的数组
        // position 当前读取值/写入值
        // mark 为某一读过的位置做标记
        // capacity 初始化时容量
        // limit 当数据写入时，limit一般与capacity相等，当读数据时，limit代表buffer中有效数据的长度
        // 满足 0<=mark<=position<=limit<=capacity
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10);
        System.out.print("Init ");
        print(byteBuffer);
        byteBuffer.put("swj".getBytes(StandardCharsets.UTF_8));
        System.out.print("After put ");
        print(byteBuffer);
        byteBuffer.flip();
        System.out.print("After flip ");
        print(byteBuffer);
        System.out.println(byteBuffer.get());
        System.out.print("After get ");
        print(byteBuffer);
        System.out.println(byteBuffer.get());
        System.out.print("After get ");
        print(byteBuffer);
        System.out.println(byteBuffer.get());
        System.out.print("After get ");
        print(byteBuffer);
        byteBuffer.clear();
        System.out.print("After clear ");
        print(byteBuffer);
    }

    private static void print(ByteBuffer buffer) {
        System.out.printf("position: %d, limit: %d, capacity: %d\n",
                buffer.position(), buffer.limit(), buffer.capacity());
    }
}
