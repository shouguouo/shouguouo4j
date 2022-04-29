package com.shouguouo.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2022-04-28 20:12:57
 */
public class LogEventBroadcaster {

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public static void main(String[] args) throws Exception {
        // 广播地址：255.255.255.255
        // 多播组通过 D 类 IP 地址和标准 UDP 端口号指定。D 类 IP 地址在 224.0.0.0 和 239.255.255.255 的范围内（包括两者）。地址 224.0.0.0 被保留，不应使用。
        // 单播地址：0.0.0.0到223.255.255.255属于单播地址
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", 14800), new File("/var/log/system.log"));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }

    public void run() throws Exception {
        Channel ch = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        for (; ; ) {
            long len = file.length();
            if (len < pointer) {
                // 文件被重置了
                pointer = len;
            } else if (len > pointer) {
                // 有日志被添加
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

}
