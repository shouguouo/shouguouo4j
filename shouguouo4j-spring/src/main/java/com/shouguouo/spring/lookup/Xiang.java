package com.shouguouo.spring.lookup;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-25 16:29:24
 */
@Component
@Data
public class Xiang {

    @Value("xiang")
    private String name;
}
