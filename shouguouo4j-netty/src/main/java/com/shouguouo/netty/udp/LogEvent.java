package com.shouguouo.netty.udp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * 消息组件
 *
 * @author shouguouo
 * @date 2022-04-28 19:49:07
 */
@Getter
@AllArgsConstructor
public final class LogEvent {

    public static final byte SEPARATOR = (byte) '|';

    private final InetSocketAddress source;

    private final long received;

    private final String logfile;

    private final String msg;

    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }
}
