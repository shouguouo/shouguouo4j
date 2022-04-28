package com.shouguouo.netty.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author shouguouo
 * @date 2022-04-24 21:59:03
 */
public class RpcServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup main = new NioEventLoopGroup(1);
        NioEventLoopGroup sub = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(main, sub)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 分隔符解码器
                            ByteBuf delimiter = Unpooled.copiedBuffer("|".getBytes(StandardCharsets.UTF_8));
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1000, delimiter));
                            // 入站String解码
                            pipeline.addLast(new StringDecoder());
                            // 出站String编码
                            pipeline.addLast(new StringEncoder());
                            // 增加业务Handler
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // 同步绑定端口
            ChannelFuture future = bootstrap.bind(12800).sync();
            // 等待关闭
            future.channel().closeFuture().sync();
        } finally {
            sub.shutdownGracefully();
            main.shutdownGracefully();
        }
    }

}
