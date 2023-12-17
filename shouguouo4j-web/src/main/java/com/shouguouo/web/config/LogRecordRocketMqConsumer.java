package com.shouguouo.web.config;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.configuration.LogRecordProperties;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author shouguouo
 * @date 2022-12-16 21:32:45
 */
@Component
@Slf4j
public class LogRecordRocketMqConsumer implements MessageListenerConcurrently {

    private final DefaultMQPushConsumer consumer;

    public LogRecordRocketMqConsumer(LogRecordProperties logRecordProperties) {
        consumer = new DefaultMQPushConsumer(logRecordProperties.getRocketMqProperties().getGroupName());
        consumer.setNamesrvAddr(logRecordProperties.getRocketMqProperties().getNamesrvAddr());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        try {
            consumer.subscribe(logRecordProperties.getRocketMqProperties().getTopic(), "*");
            consumer.registerMessageListener(this);
            consumer.start();
        } catch (Exception e) {
            log.error("logRecord消费者启动失败", e);
        }
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        int index = 0;
        try {
            for (; index < msgs.size(); index++) {
                MessageExt messageExt = msgs.get(index);
                LogDTO logDto = JSON.parseObject(new String(messageExt.getBody(), StandardCharsets.UTF_8), LogDTO.class);
                log.info("logDto from rocketMq: {}", logDto);
            }
        } catch (Exception e) {
            log.error("消息消费失败：", e);
        } finally {
            if (index < msgs.size()) {
                context.setAckIndex(index + 1);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


    @PreDestroy
    public void destroy() {
        if (consumer != null) {
            consumer.shutdown();
            log.info("摧毁消费者");
        }
    }
}
