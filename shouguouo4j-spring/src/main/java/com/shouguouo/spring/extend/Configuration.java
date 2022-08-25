package com.shouguouo.spring.extend;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author shouguouo
 * @date 2022-08-24 20:04:53
 */
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "com.shouguouo.spring.extend")
@Import(ExtendBean.class)
public class Configuration {

}
