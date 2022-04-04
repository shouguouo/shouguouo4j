package com.shouguouo.web.rocketmq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shouguouo
 * @date 2022-04-04 12:11:24
 */
@Configuration
public class MQConfiguration {

    @ConditionalOnProperty(name = "spring.rocketmq.enable", havingValue = "true")
    @Bean
    public MQProducer mqProducer() {
        return new MQProducer();
    }

    @ConditionalOnProperty(name = "spring.rocketmq.enable", havingValue = "true")
    @Bean
    public MQPushConsumer mqPushConsumer() {
        return new MQPushConsumer();
    }
}
