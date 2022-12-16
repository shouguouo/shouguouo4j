package com.shouguouo.web.controller;

import com.shouguouo.web.rocketmq.MQProducer;
import com.shouguouo.web.rocketmq.domain.UserIdModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author shouguouo
 * @date 2021-12-24 21:34:41
 */
@RestController
@RequestMapping("/mq")
// @ConditionalOnBean(value = { MQProducer.class, MQPushConsumer.class })
public class MessageQueueController {

    @Value("${spring.rocketmq.topic}")
    private String topic;

    @Autowired
    private MQProducer mqProducer;

    @GetMapping("/userIdModifier/{old}/{new}")
    public boolean sendMessage(
            @PathVariable("old") Long oldUserId,
            @PathVariable("new") Long newUserId,
            @RequestParam("queueId") Integer queueId) {
        UserIdModifier userIdModifier = new UserIdModifier();
        userIdModifier.setOldUserId(oldUserId);
        userIdModifier.setNewUserId(newUserId);
        if (queueId == -1) {
            mqProducer.sendMessage(userIdModifier, topic, null, null, null);
            return true;
        }
        mqProducer.sendMessage(userIdModifier, topic, null, queueId.toString(), (mqs, msg, arg) -> {
            int i = Integer.parseInt((String) arg);
            return mqs.get(i % mqs.size());
        });
        return true;
    }

    @GetMapping("/userIdModifier/{old}/{new}/{count}")
    public boolean sendMessageBatch(
            @PathVariable("old") Long oldUserId,
            @PathVariable("new") Long newUserId,
            @PathVariable("count") Long count) {
        List<Object> modifierList = LongStream.rangeClosed(1, count).mapToObj(cur -> {
            UserIdModifier userIdModifier = new UserIdModifier();
            userIdModifier.setOldUserId(oldUserId);
            userIdModifier.setNewUserId(newUserId);
            userIdModifier.setVersion(cur);
            return userIdModifier;
        }).collect(Collectors.toList());
        mqProducer.sendMessageBatch(modifierList, topic, null, null);
        return true;
    }
}
