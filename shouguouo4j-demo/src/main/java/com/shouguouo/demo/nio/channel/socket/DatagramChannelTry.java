package com.shouguouo.demo.nio.channel.socket;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.shouguouo.common.util.ExecutorUtils;
import com.shouguouo.common.util.OutputUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * TimeServer
 * TimeClient
 * 可模拟多个主机向时间服务器同步时间，比较时间戳差
 *
 * @author shouguouo
 * @date 2022-04-03 21:54:29
 */
public class DatagramChannelTry {

    private static final List<String> HOSTS = Lists.newArrayList("127.0.0.1");

    private static final int DEFAULT_TIME_PORT = 37;

    private static final int ELAPSE_TO_SHUTDOWN = 10;

    private static final long DIFF_1900 = 2208988800L;

    public static void main(String[] argv) throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CountDownLatch cdl = new CountDownLatch(2);
        ExecutorUtils.execute(() -> {
            try {
                TimeServer server = new TimeServer(DEFAULT_TIME_PORT);
                server.listen();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cdl.countDown();
            }
        });
        TimeUnit.SECONDS.sleep(1);
        ExecutorUtils.execute(() -> {
            try {
                TimeClient client = new TimeClient();
                client.sendRequests();
                client.getReplies();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cdl.countDown();
            }
        });
        cdl.await();
        OutputUtils.printlnWithCurrentThread("shutdown...elapsed: " + stopwatch.stop());
        ExecutorUtils.shutdown();
    }

    static class TimeServer {

        protected DatagramChannel channel;

        public TimeServer(int port)
                throws Exception {
            this.channel = DatagramChannel.open();
            this.channel.bind(new InetSocketAddress(port));
            // 非阻塞
            this.channel.configureBlocking(false);
            OutputUtils.printlnWithCurrentThread("Listening on port " + port
                    + " for time requests");
        }

        public void listen() throws Exception {
            // Allocate a buffer to hold a long value
            ByteBuffer longBuffer = ByteBuffer.allocate(8);
            // Assure big-endian (network) byte order
            longBuffer.order(ByteOrder.BIG_ENDIAN);
            // Zero the whole buffer to be sure
            longBuffer.putLong(0, 0);
            // Position to first byte of the low-order 32 bits
            longBuffer.position(4);
            // Slice the buffer; gives view of the low-order 32 bits
            ByteBuffer buffer = longBuffer.slice();
            Stopwatch start = Stopwatch.createStarted();
            // 无连接程序自动关闭
            while (start.elapsed().compareTo(Duration.ofSeconds(ELAPSE_TO_SHUTDOWN)) < 0) {
                buffer.clear();
                SocketAddress sa = this.channel.receive(buffer);
                if (sa == null) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    continue; // defensive programming
                }
                // Ignore content of received datagram per RFC 868
                OutputUtils.printlnWithCurrentThread("Time request from " + sa);
                buffer.clear(); // sets pos/limit correctly
                // Set 64-bit value; slice buffer sees low 32 bits
                longBuffer.putLong(0,
                        (System.currentTimeMillis() / 1000) + DIFF_1900);
                this.channel.send(buffer, sa);
                start.reset();
                start.start();
            }
        }
    }

    static class TimeClient {

        protected int port = DEFAULT_TIME_PORT;

        protected List<InetSocketAddress> remoteHosts;

        protected DatagramChannel channel;

        public TimeClient() throws IOException {
            init();
            this.channel = DatagramChannel.open();
        }

        protected void init() {
            remoteHosts = new LinkedList<>();
            for (String host : HOSTS) {
                // Create an address object for the hostname
                InetSocketAddress sa = new InetSocketAddress(host, port);
                // Validate that it has an address
                if (sa.getAddress() == null) {
                    OutputUtils.printlnWithCurrentThread("Cannot resolve address: "
                            + sa);
                    continue;
                }
                remoteHosts.add(sa);
            }
        }

        protected InetSocketAddress receivePacket(DatagramChannel channel, ByteBuffer buffer)
                throws Exception {
            buffer.clear();
            // Receive an unsigned 32-bit, big-endian value
            return ((InetSocketAddress) channel.receive(buffer));
        }

        // Send time requests to all the supplied hosts
        protected void sendRequests() throws Exception {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            for (InetSocketAddress remoteHost : remoteHosts) {
                OutputUtils.printlnWithCurrentThread("Requesting time from "
                        + remoteHost.getHostName() + ":" + remoteHost.getPort());
                // Make it empty (see RFC868)
                buffer.clear().flip();
                // Fire and forget
                channel.send(buffer, remoteHost);
            }
        }

        // Receive any replies that arrive
        public void getReplies() throws Exception {
            // Allocate a buffer to hold a long value
            ByteBuffer longBuffer = ByteBuffer.allocate(8);
            // Assure big-endian (network) byte order
            longBuffer.order(ByteOrder.BIG_ENDIAN);
            // Zero the whole buffer to be sure
            longBuffer.putLong(0, 0);
            // Position to first byte of the low-order 32 bits
            longBuffer.position(4);
            // Slice the buffer; gives view of the low-order 32 bits
            ByteBuffer buffer = longBuffer.slice();
            int expect = remoteHosts.size();
            int replies = 0;
            OutputUtils.printlnWithCurrentThread("");
            OutputUtils.printlnWithCurrentThread("Waiting for replies...");
            do {
                InetSocketAddress sa;
                // 阻塞
                sa = receivePacket(channel, buffer);
                buffer.flip();
                replies++;
                printTime(longBuffer.getLong(0), sa);
                if (replies == expect) {
                    OutputUtils.printlnWithCurrentThread("All packets answered");
                    break;
                }
                // Some replies haven't shown up yet
                OutputUtils.printlnWithCurrentThread("Received " + replies
                        + " of " + expect + " replies");
            } while (1 == 2);
        }

        // Print info about a received time reply
        protected void printTime(long remote1900, InetSocketAddress sa) {
            // local time as seconds since Jan 1, 1970
            long local = System.currentTimeMillis() / 1000;
            // remote time as seconds since Jan 1, 1970
            long remote = remote1900 - DIFF_1900;
            Date remoteDate = new Date(remote * 1000);
            Date localDate = new Date(local * 1000);
            long skew = remote - local;
            OutputUtils.printlnWithCurrentThread("Reply from "
                    + sa.getHostName() + ":" + sa.getPort());
            OutputUtils.printlnWithCurrentThread(" there: " + remoteDate);
            OutputUtils.printlnWithCurrentThread(" here: " + localDate);
            OutputUtils.printWithCurrentThread(" skew: ");
            if (skew == 0) {
                OutputUtils.printlnWithCurrentThread("none");
            } else if (skew > 0) {
                OutputUtils.printlnWithCurrentThread(skew + " seconds ahead");
            } else {
                OutputUtils.printlnWithCurrentThread((-skew) + " seconds behind");
            }
        }
    }

}
