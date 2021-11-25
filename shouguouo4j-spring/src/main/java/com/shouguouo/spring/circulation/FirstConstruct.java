package com.shouguouo.spring.circulation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-25 09:51:38
 */
@Component
@Getter
@Setter
public class FirstConstruct {

    private SecondConstruct secondConstruct;

    @Value("first")
    private String name;

    @Autowired
    public FirstConstruct(SecondConstruct secondConstruct) {
        this.secondConstruct = secondConstruct;
    }
}
