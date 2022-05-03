package com.shouguouo.demo.nio.reactor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

/**
 * IO任务
 *
 * @author shouguouo
 * @date 2022-05-03 14:26:56
 */
@Data
@AllArgsConstructor
public class Task implements Serializable {

    private SocketChannel sc;

    private int op;

    private Object data;

}
