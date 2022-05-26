package com.shouguouo.dubbo.demo;

import org.apache.dubbo.common.config.OrderedPropertiesProvider;
import org.apache.dubbo.common.constants.CommonConstants;

/**
 * @author shouguouo
 * @date 2022-05-26 20:17:24
 */
public class Properties implements OrderedPropertiesProvider {

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public java.util.Properties initProperties() {
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(CommonConstants.ENABLE_NATIVE_JAVA_GENERIC_SERIALIZE, "true");
        return properties;
    }
}
