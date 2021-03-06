package com.shouguouo.demo.nio.channel.file;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

/**
 * run with program arguments "-r /tmp/locktest.dat" and "-w /tmp/locktest.dat"
 *
 * @author shouguouo
 * @date 2022-03-27 12:50:27
 */
public class FileLockTry {

    private static final int SIZEOF_INT = 4;

    private static final int INDEX_START = 0;

    private static final int INDEX_COUNT = 10;

    private static final int INDEX_SIZE = INDEX_COUNT * SIZEOF_INT;

    private final ByteBuffer buffer = ByteBuffer.allocate(INDEX_SIZE);

    private final IntBuffer indexBuffer = buffer.asIntBuffer();

    private final Random rand = new Random();

    private int idxval = 1;

    private int lastLineLen = 0;

    public static void main(String[] argv) throws Exception {
        boolean writer;
        String filename;
        if (argv.length != 2) {
            System.out.println("Usage: [ -r | -w ] filename");
            return;
        }
        writer = "-w".equals(argv[0]);
        filename = argv[1];
        RandomAccessFile raf = new RandomAccessFile(filename,
                (writer) ? "rw" : "r");
        FileChannel fc = raf.getChannel();
        FileLockTry lockTry = new FileLockTry();
        if (writer) {
            lockTry.doUpdates(fc);
        } else {
            lockTry.doQueries(fc);
        }
    }

    void doQueries(FileChannel fc)
            throws Exception {
        while (true) {
            println("trying for shared lock...");
            FileLock lock = fc.lock(INDEX_START, INDEX_SIZE, true);
            int reps = rand.nextInt(60) + 20;
            for (int i = 0; i < reps; i++) {
                int n = rand.nextInt(INDEX_COUNT);
                int position = INDEX_START + (n * SIZEOF_INT);
                buffer.clear();
                fc.read(buffer, position);
                int value = indexBuffer.get(n);
                println("Index entry " + n + "=" + value);
                // Pretend to be doing some work
                Thread.sleep(100);
            }
            lock.release();
            println("<sleeping>");
            Thread.sleep(rand.nextInt(3000) + 500);
        }
    }

    void doUpdates(FileChannel fc)
            throws Exception {
        while (true) {
            println("trying for exclusive lock...");
            FileLock lock = fc.lock(INDEX_START,
                    INDEX_SIZE, false);
            updateIndex(fc);
            lock.release();
            println("<sleeping>");
            Thread.sleep(rand.nextInt(2000) + 500);
        }
    }

    private void updateIndex(FileChannel fc)
            throws Exception {
        // "indexBuffer" is an int view of "buffer"
        indexBuffer.clear();
        for (int i = 0; i < INDEX_COUNT; i++) {
            idxval++;
            println("Updating index " + i + "=" + idxval);
            indexBuffer.put(idxval);
            // Pretend that this is really hard work
            Thread.sleep(500);
        }
        // leaves position and limit correct for whole buffer
        buffer.clear();
        fc.write(buffer, INDEX_START);
    }

    private void println(String msg) {
        System.out.print("\r ");
        System.out.print(msg);
        for (int i = msg.length(); i < lastLineLen; i++) {
            System.out.print(" ");
        }
        System.out.print("\r");
        System.out.flush();
        lastLineLen = msg.length();
    }

}
