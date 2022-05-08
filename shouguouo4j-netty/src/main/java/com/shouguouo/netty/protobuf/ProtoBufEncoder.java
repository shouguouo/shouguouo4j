package com.shouguouo.netty.protobuf;

import com.shouguouo.netty.protobuf.domain.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @author shouguouo
 * @date 2022-05-07 23:17:30
 */
public class ProtoBufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    private static final String MAGIC = "SHOUGUOUO";

    private static final int VERSION = 100;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMsg.Message msg, ByteBuf byteBuf) {
        byte[] bytes = msg.toByteArray();
        int len = bytes.length;
        // 写入长度、魔数、版本号
        byteBuf.writeInt(len);
        byteBuf.writeBytes(MAGIC.getBytes(StandardCharsets.UTF_8));
        byteBuf.writeShort(VERSION);
        byteBuf.writeBytes(bytes);
    }
}
