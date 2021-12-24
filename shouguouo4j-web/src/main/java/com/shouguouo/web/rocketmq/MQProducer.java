package com.shouguouo.web.rocketmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shouguouo
 * @date 2021-12-24 20:11:37
 */
@Component
@Slf4j
public class MQProducer {

    @Value("${spring.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${spring.rocketmq.producerGroup}")
    private String producerGroup;

    private DefaultMQProducer producer;

    @PostConstruct
    public void start() {
        log.info("启动生产者");
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("生产者启动失败", e);
        }
    }

    public void sendMessage(Object data, String topic, String tags, String keys, MessageQueueSelector selector) {
        if (data == null || topic == null || topic.isEmpty()) {
            throw new RuntimeException("message data or topic must not be empty");
        }
        try {
            byte[] body = JSON.toJSONString(data).getBytes(StandardCharsets.UTF_8);
            Message message = new Message(topic, tags, keys, body);
            SendCallback sendCallback = new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("发送消息：{}", sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    log.error("发送消息失败：", e);
                }
            };
            if (selector == null) {
                producer.send(message, sendCallback);
            } else {
                producer.send(message, selector, keys, sendCallback);
            }

        } catch (Exception e) {
            log.error("发送消息失败：", e);
        }
    }

    public void sendMessageBatch(List<Object> dataList, String topic, String tags, String keys) {
        if (dataList == null || dataList.isEmpty() || topic == null || topic.isEmpty()) {
            throw new RuntimeException("message data or topic must not be empty");
        }
        try {
            List<Message> messageList = dataList.stream().map(data -> {
                byte[] body = JSON.toJSONString(data).getBytes(StandardCharsets.UTF_8);
                return new Message(topic, tags, keys, body);
            }).collect(Collectors.toList());
            producer.send(messageList, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("发送消息：{}", sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    log.error("发送消息失败：", e);
                }
            });
        } catch (Exception e) {
            log.error("发送消息失败：", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (producer != null) {
            producer.shutdown();
            log.info("摧毁生产者");
        }
    }

}
