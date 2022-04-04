package com.shouguouo.demo.nio;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 使用Pipe进行线程间数据的单向传输
 *
 * @author shouguouo
 * @date 2022-04-04 20:12:06
 */
public class PipeTry {

    public static void main(String[] args) throws Exception {
        // Wrap a channel around stdout
        WritableByteChannel out = Channels.newChannel(System.out);
        // Start worker and get read end of channel
        ReadableByteChannel workerChannel = startWorker(10);

        ByteBuffer buffer = ByteBuffer.allocate(100);
        TimeUnit.SECONDS.sleep(3);
        // 从SourceChannel读取到buffer
        while (workerChannel.read(buffer) >= 0) {
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
    }

    // This method could return a SocketChannel or
    // FileChannel instance just as easily
    private static ReadableByteChannel startWorker(int reps)
            throws Exception {
        Pipe pipe = Pipe.open();
        // 全部写到SinkChannel
        Worker worker = new Worker(pipe.sink(), reps);
        worker.start();
        // 返回SourceChannel
        return (pipe.source());
    }

    /**
     * A worker thread object which writes data down a channel.
     * Note: this object knows nothing about Pipe, uses only a
     * generic WritableByteChannel.
     */
    private static class Worker extends Thread {

        private final int reps;

        WritableByteChannel channel;

        private String[] products = {
                "No good deed goes unpunished",
                "To be, or what?",
                "No matter where you go, there you are",
                "Just say \"Yo\"",
                "My karma ran over my dogma"
        };

        private Random rand = new Random();

        Worker(WritableByteChannel channel, int reps) {
            this.channel = channel;
            this.reps = reps;
        }

        // Thread execution begins here
        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            try {
                for (int i = 0; i < this.reps; i++) {
                    doSomeWork(buffer);
                    // channel may not take it all at once
                    while (channel.write(buffer) > 0) {
                        // empty
                    }
                }
                this.channel.close();
            } catch (Exception e) {
                // easy way out; this is demo code
                e.printStackTrace();
            }
        }

        private void doSomeWork(ByteBuffer buffer) {
            int product = rand.nextInt(products.length);
            buffer.clear();
            buffer.put(products[product].getBytes());
            buffer.put("\r\n".getBytes());
            buffer.flip();
        }
    }
}
