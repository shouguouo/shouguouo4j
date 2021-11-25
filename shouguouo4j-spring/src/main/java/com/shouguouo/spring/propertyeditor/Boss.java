package com.shouguouo.spring.propertyeditor;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-25 14:46:02
 */
@Data
@Component
public class Boss {

    private String name;

    @Value("红旗CA72,200,20000.00")
    private Car car;
}
