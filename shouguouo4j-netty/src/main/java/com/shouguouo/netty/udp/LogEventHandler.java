package com.shouguouo.netty.udp;

import com.shouguouo.common.util.TimeUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shouguouo
 * @date 2022-04-28 22:40:50
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
        String builder = TimeUtils.format(event.getReceived())
                + " ["
                + event.getSource().toString()
                + "] ["
                + event.getLogfile()
                + "] : "
                + event.getMsg();
        System.out.println(builder);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
