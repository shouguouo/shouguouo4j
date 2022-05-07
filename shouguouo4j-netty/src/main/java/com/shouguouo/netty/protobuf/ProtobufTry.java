package com.shouguouo.netty.protobuf;

import com.shouguouo.common.util.OutputUtils;
import com.shouguouo.netty.protobuf.domain.MsgProtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author shouguouo
 * @date 2022-05-07 23:00:57
 */
public class ProtobufTry {

    public static void main(String[] args) throws IOException {
        MsgProtos.Msg msg = buildMsg();
        // 写入
        OutputUtils.cuttingLine("写入");
        OutputUtils.println("id: " + msg.getId());
        OutputUtils.println("content: " + msg.getContent());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        msg.writeDelimitedTo(outputStream);
        // 读出
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        MsgProtos.Msg inMsg = MsgProtos.Msg.parseDelimitedFrom(inputStream);
        OutputUtils.cuttingLine("读出");
        OutputUtils.println("id: " + inMsg.getId());
        OutputUtils.println("content: " + inMsg.getContent());
        OutputUtils.cuttingLine("比较");
        OutputUtils.println("isEquals: " + msg.equals(inMsg));
        OutputUtils.println("isSame: " + (msg == inMsg));
    }

    private static MsgProtos.Msg buildMsg() {
        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
        builder.setId(1000);
        builder.setContent("你好，Hello!");
        return builder.build();
    }
}
