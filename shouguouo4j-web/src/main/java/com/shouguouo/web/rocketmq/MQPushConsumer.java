package com.shouguouo.web.rocketmq;

import com.alibaba.fastjson.JSON;
import com.shouguouo.web.rocketmq.domain.UserIdModifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author shouguouo
 * @date 2021-12-24 21:12:38
 */
@Component
@Slf4j
public class MQPushConsumer implements MessageListenerConcurrently {

    @Value("${spring.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${spring.rocketmq.consumerGroup}")
    private String consumerGroup;

    @Value("${spring.rocketmq.topic}")
    private String consumerTopic;

    private DefaultMQPushConsumer consumer;

    @PostConstruct
    public void start() {
        log.info("启动消费者");
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        try {
            consumer.subscribe(consumerTopic, "*");
            consumer.registerMessageListener(this);
            consumer.start();
        } catch (MQClientException e) {
            log.error("消费者启动失败", e);
        }
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        int index = 0;
        try {
            for (; index < msgs.size(); index++) {
                MessageExt messageExt = msgs.get(index);
                String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
                UserIdModifier userIdModifier = JSON.parseObject(messageBody, UserIdModifier.class);
                log.info("消费者收到新消息：{}, {}, {}, {}, {}",
                        messageExt.getMsgId(),
                        messageExt.getTopic(),
                        messageExt.getTags(),
                        messageExt.getKeys(),
                        userIdModifier);
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
