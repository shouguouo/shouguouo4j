package com.shouguouo.netty.udp;

import com.shouguouo.common.util.OutputUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * @author shouguouo
 * @date 2022-04-28 22:54:01
 */
public class LogEventMonitor {

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    public LogEventMonitor(InetSocketAddress address) {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new LogEventDecoder()).addLast(new LogEventHandler());
                    }
                }).localAddress(address);
    }

    public static void main(String[] args) throws InterruptedException {
        LogEventMonitor logEventMonitor = new LogEventMonitor(new InetSocketAddress(14800));
        try {
            Channel channel = logEventMonitor.bind();
            OutputUtils.cuttingLine("LogEventMonitor running");
            channel.closeFuture().sync();
        } finally {
            logEventMonitor.stop();
        }

    }

    public Channel bind() {
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }
}
