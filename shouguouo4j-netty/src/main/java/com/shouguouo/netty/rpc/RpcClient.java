package com.shouguouo.netty.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shouguouo
 * @date 2022-04-24 22:26:45
 */
public class RpcClient {

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    private final Bootstrap bootstrap;

    private volatile Channel channel;

    public RpcClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
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
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });
            // 同步等待连接
            ChannelFuture future = bootstrap.connect("127.0.0.1", 12800).sync();
            if (future.isDone() && future.isSuccess()) {
                this.channel = future.channel();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String msg) {
        channel.writeAndFlush(msg);
    }

    public void close() {
        if (null != bootstrap) {
            bootstrap.config().group().shutdownGracefully();
        }
        if (null != channel) {
            channel.close();
        }
    }

    private String generatorFrame(String msg, String reqId) {
        return msg + ":" + reqId + "|";
    }

    public CompletableFuture<String> rpcAsyncCall(String msg) {
        // 生成invokeId用于handler内关联
        CompletableFuture<String> future = new CompletableFuture<>();
        String id = INVOKE_ID.getAndIncrement() + "";
        msg = generatorFrame(msg, id);
        this.sendMsg(msg);
        FutureMapUtils.put(id, future);
        return future;
    }

    public String rpcSyncCall(String msg) throws InterruptedException, ExecutionException, TimeoutException {
        // 同步等待 超时时间30秒
        return rpcAsyncCall(msg).get(30, TimeUnit.SECONDS);
    }
}
