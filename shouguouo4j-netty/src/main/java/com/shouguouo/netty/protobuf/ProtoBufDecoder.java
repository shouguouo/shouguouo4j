package com.shouguouo.netty.protobuf;

import com.shouguouo.netty.protobuf.domain.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author shouguouo
 * @date 2022-05-07 23:36:50
 */
public class ProtoBufDecoder extends ByteToMessageDecoder {

    private static final byte[] MAGIC = "SHOUGUOUO".getBytes(StandardCharsets.UTF_8);

    private static final int VERSION = 100;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        // 标记当前读取位置
        in.markReaderIndex();
        if (in.readableBytes() < 4) {
            return;
        }
        int len = in.readInt();
        if (len < 0) {
            channelHandlerContext.close();
            return;
        }
        if (len + MAGIC.length + 2 > in.readableBytes()) {
            // 小于预期长度 重置读取位置返回
            in.resetReaderIndex();
            return;
        }
        // 验证魔数
        byte[] magicBytes = new byte[MAGIC.length];
        in.readBytes(magicBytes);
        if (!Arrays.equals(magicBytes, MAGIC)) {
            // 魔数验证不通过 关闭连接
            channelHandlerContext.close();
            return;
        }
        // 验证版本
        short version = in.readShort();
        if (version != VERSION) {
            // 版本验证不通过 关闭连接
            channelHandlerContext.close();
            return;
        }
        byte[] content = new byte[len];
        in.readBytes(content);
        ProtoMsg.Message msg = ProtoMsg.Message.parseFrom(content);
        if (msg != null) {
            list.add(msg);
        }
    }
}
