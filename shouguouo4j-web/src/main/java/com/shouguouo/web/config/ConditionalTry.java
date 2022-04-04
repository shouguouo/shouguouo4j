package com.shouguouo.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author shouguouo
 * @date 2022-04-04 12:25:06
 */
@Configuration
@Slf4j
public class ConditionalTry {

    @ConditionalOnProperty(name = "config.enable", havingValue = "true", matchIfMissing = true)
    @Bean
    public EnabledBean enableBean() {
        log.info("Create Bean EnabledBean");
        return new EnabledBean();
    }

    @ConditionalOnBean(EnabledBean.class)
    @Bean
    @Conditional(InetSocketAddressCondition.class)
    public InetSocketAddress inetSocketAddress(@Value("${config.host}") String host, @Value("${config.port}") Integer port) {
        if (StringUtils.isEmpty(host) || port == null) {
            log.error("host and port must not be empty!");
            throw new RuntimeException();
        }
        log.info("Create Bean InetSocketAddress");
        return new InetSocketAddress(host, port);
    }

    private static class InetSocketAddressCondition extends AllNestedConditions {

        public InetSocketAddressCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(prefix = "config", value = { "host", "port" })
        static class InetSocketAddressConfig {

        }
    }

}
