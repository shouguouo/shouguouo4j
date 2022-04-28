package com.shouguouo.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author shouguouo
 * @date 2022-04-28 12:40:41
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 解编码器，HttpRequest、HttpContent 和 LastHttpContent
                .addLast(new HttpServerCodec())
                // 写入一个文件的内容
                .addLast(new ChunkedWriteHandler())
                // 聚合为单个FullHttpRequest/FullHttpResponse，便于下个handler处理一个完整的http请求
                .addLast(new HttpObjectAggregator(64 * 1024))
                // 处理FullHttpRequest
                .addLast(new HttpRequestHandler("/ws"))
                // 根据WebSocket规范的要求，处理WebSocket升级握手、PingWebSocketFrame、PongWebSocketFrame和CloseWebSocketFrame
                .addLast(new WebSocketServerProtocolHandler("/ws"))
                // 处理TextWebSocketFrame和握手完成事件
                .addLast(new TextWebSocketFrameHandler(group));
    }
}
