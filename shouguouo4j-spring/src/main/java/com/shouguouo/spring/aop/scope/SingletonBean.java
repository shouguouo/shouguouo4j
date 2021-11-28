package com.shouguouo.spring.aop.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-28 13:29:54
 */
@Component
@Scope("singleton")
public class SingletonBean {

    @Autowired
    private PrototypeBean prototypeBean;

    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }
}
