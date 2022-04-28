package com.shouguouo.netty.rpc;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2022-04-24 22:26:14
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public String generatorFrame(String msg, String reqId) {
        return msg + ":" + reqId + "|";
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        // 获取消息体 解析出id
        String str = (String) msg;
        String reqId = str.split(":")[1];
        // 模拟返回结果
        String resp = generatorFrame("Hello " + str.split(":")[0] + "\r\n", reqId);
        // 模拟请求处理耗时
        TimeUnit.SECONDS.sleep(2);
        // 写入通道
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
    }
}
