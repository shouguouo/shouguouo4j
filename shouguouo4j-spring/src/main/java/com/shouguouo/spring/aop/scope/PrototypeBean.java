package com.shouguouo.spring.aop.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * 不声明ScopedProxyMode.TARGET_CLASS则SingletonBean每次get时不会重新实例新的Bean
 *
 * @author shouguouo
 * @date 2021-11-28 13:29:28
 * @see SingletonBean#getPrototypeBean()
 */
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrototypeBean {

}
