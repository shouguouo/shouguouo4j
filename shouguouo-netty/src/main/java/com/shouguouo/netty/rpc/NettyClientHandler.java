package com.shouguouo.netty.rpc;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CompletableFuture;

/**
 * @author shouguouo
 * @date 2022-04-25 09:16:08
 */
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String[] split = ((String) msg).split(":");
        if (split.length < 2) {
            return;
        }
        CompletableFuture<String> future = FutureMapUtils.remove(split[1]);
        if (future != null) {
            future.complete(split[0]);
        }
    }
}
