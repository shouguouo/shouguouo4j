package com.shouguouo.netty.protobuf;

import com.shouguouo.netty.protobuf.domain.ProtoMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.UUID;

/**
 * @author shouguouo
 * @date 2022-05-08 20:44:36
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ProtoBufEncoder());
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 10086).sync();

            ProtoMsg.LoginRequest.Builder loginBuilder = ProtoMsg.LoginRequest.newBuilder();
            loginBuilder.setUid("shouguouo");
            loginBuilder.setDeviceId("mac");
            loginBuilder.setToken(UUID.randomUUID().toString());
            loginBuilder.setPlatform(1);
            loginBuilder.setAppVersion("01");
            ProtoMsg.Message.Builder builder = ProtoMsg.Message.newBuilder();
            builder.setType(ProtoMsg.HeadType.LOGIN_REQUEST);
            builder.setSequence(System.currentTimeMillis());
            builder.setSessionId("NO1");
            builder.setLoginRequest(loginBuilder);
            ProtoMsg.Message message = builder.build();
            ChannelFuture writeFuture = future.channel().writeAndFlush(message);
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
