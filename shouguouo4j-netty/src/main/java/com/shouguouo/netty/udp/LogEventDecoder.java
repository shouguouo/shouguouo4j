package com.shouguouo.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author shouguouo
 * @date 2022-04-28 21:44:43
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf buf = datagramPacket.content();
        int idx = buf.indexOf(0, buf.readableBytes(), LogEvent.SEPARATOR);
        String fileName = buf.slice(0, idx).toString(StandardCharsets.UTF_8);
        String logMsg = buf.slice(idx + 1, buf.readableBytes() - idx - 1).toString(StandardCharsets.UTF_8);
        LogEvent event = new LogEvent(datagramPacket.sender(), System.currentTimeMillis(), fileName, logMsg);
        list.add(event);
    }
}