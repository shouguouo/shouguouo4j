package com.shouguouo.spring.circulation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-25 09:51:38
 */
@Component
@Getter
@Setter
@Scope("prototype")
public class FirstPrototype {

    @Autowired
    private SecondPrototype secondPrototype;

    @Value("first")
    private String name;
}
