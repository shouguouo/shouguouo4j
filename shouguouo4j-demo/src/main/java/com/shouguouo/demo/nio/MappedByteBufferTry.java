package com.shouguouo.demo.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer底层依赖操作系统的mmap系统调用
 *
 * @author shouguouo
 * @date 2022-03-27 20:53:37
 */
public class MappedByteBufferTry {

    /**
     * getconf PAGESIZE
     * 20款m1 mbp 16kb
     */
    private static final int PAGE_SIZE = 16 * 1024;

    public static void main(String[] args) throws Exception {
        // Create a temp file and get a channel connected to it
        File tempFile = File.createTempFile("mmaptest", null);
        RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer temp = ByteBuffer.allocate(100);
        // Put something in the file, starting at location 0
        temp.put("This is the file content".getBytes());
        temp.flip();
        channel.write(temp, 0);
        // Put something else in the file, starting at location 8192.
        // 8192 is 8 KB, almost certainly a different memory/FS page.
        // This may cause a file hole, depending on the
        // filesystem page size.
        temp.clear();
        temp.put("This is more file content".getBytes());
        temp.flip();
        channel.write(temp, PAGE_SIZE);

        // Create three types of mappings to the same file
        MappedByteBuffer ro = channel.map(
                FileChannel.MapMode.READ_ONLY, 0, channel.size());
        MappedByteBuffer rw = channel.map(
                FileChannel.MapMode.READ_WRITE, 0, channel.size());
        MappedByteBuffer cow = channel.map(
                FileChannel.MapMode.PRIVATE, 0, channel.size());

        // the buffer states before any modifications
        System.out.println("Begin");
        showBuffers(ro, rw, cow);
        // Modify the copy-on-write buffer
        cow.position(8);
        cow.put("COW".getBytes());
        System.out.println("Change to COW buffer");
        showBuffers(ro, rw, cow);
        // Modify the read/write buffer
        rw.position(9);
        rw.put(" R/W ".getBytes());
        rw.position(PAGE_SIZE + 2);
        rw.put(" R/W ".getBytes());
        rw.force();
        System.out.println("Change to R/W buffer");
        showBuffers(ro, rw, cow);
        // Write to the file through the channel; hit both pages
        // but no effect to cow in this compute
        // 网上没有明确的资料说明什么情况下，MapMode.PRIVATE时其他进程对文件的修改会反映在PRIVATE的MappedByteBuffer上
        // 比较靠谱的说明：It is unspecified whether changes made to the file after the mmap() call are visible in the mapped region. ref:https://man7.org/linux/man-pages/man2/mmap.2.html
        // uname -a Darwin shouguouo.local 21.3.0 Darwin Kernel Version 21.3.0: Wed Jan  5 21:37:58 PST 2022; root:xnu-8019.80.24~20/RELEASE_ARM64_T8101 arm64
        temp.clear();
        temp.put("Channel write ".getBytes());
        temp.flip();
        channel.write(temp, 0);
        temp.rewind();
        channel.write(temp, PAGE_SIZE + 10);
        System.out.println("Write on channel");
        showBuffers(ro, rw, cow);
        // Modify the copy-on-write buffer again
        cow.position(PAGE_SIZE + 15);
        cow.put(" COW2 ".getBytes());
        System.out.println("Second change to COW buffer");
        showBuffers(ro, rw, cow);
        // Modify the read/write buffer
        rw.position(0);
        rw.put(" R/W2 ".getBytes());
        rw.position(PAGE_SIZE + 18);
        rw.put(" R/W2 ".getBytes());
        rw.force();
        System.out.println("Second change to R/W buffer");
        showBuffers(ro, rw, cow);
        // cleanup
        channel.close();
        file.close();
        tempFile.delete();
    }

    // Show the current content of the three buffers
    public static void showBuffers(ByteBuffer ro, ByteBuffer rw,
            ByteBuffer cow) throws Exception {
        dumpBuffer("R/O", ro);
        dumpBuffer("R/W", rw);
        dumpBuffer("COW", cow);
        System.out.println("");
    }

    // Dump buffer content, counting and skipping nulls
    public static void dumpBuffer(String prefix, ByteBuffer buffer)
            throws Exception {
        System.out.print(prefix + ": '");
        int nulls = 0;
        int limit = buffer.limit();
        for (int i = 0; i < limit; i++) {
            char c = (char) buffer.get(i);
            if (c == '\u0000') {
                // 文件空洞
                nulls++;
                continue;
            }
            if (nulls != 0) {
                System.out.print("|[" + nulls
                        + " nulls]|");
                nulls = 0;
            }
            System.out.print(c);
        }
        System.out.println("'");
    }
}
