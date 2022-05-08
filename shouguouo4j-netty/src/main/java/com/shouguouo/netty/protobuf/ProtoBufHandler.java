package com.shouguouo.netty.protobuf;

import com.shouguouo.common.util.OutputUtils;
import com.shouguouo.netty.protobuf.domain.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author shouguouo
 * @date 2022-05-08 07:45:57
 */
public class ProtoBufHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ProtoMsg.Message) {
            OutputUtils.println("received: " + msg);
        }
    }
}
