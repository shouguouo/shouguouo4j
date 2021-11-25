package com.shouguouo.spring.circulation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-25 09:52:53
 */
@Component
@Getter
@Setter
public class SecondConstruct {

    private FirstConstruct firstConstruct;

    @Value("second")
    private String name;

    @Autowired
    public SecondConstruct(FirstConstruct firstConstruct) {
        this.firstConstruct = firstConstruct;
    }
}
