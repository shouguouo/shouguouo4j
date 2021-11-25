package com.shouguouo.spring.circulation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author shouguouo
 * @date 2021-11-25 09:47:33
 */
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "com.shouguouo.spring.circulation", includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Construct") },
        useDefaultFilters = false)
public class ConfigurationConstruct {

}
