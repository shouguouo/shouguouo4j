package com.shouguouo.spring.lookup;

import lombok.Data;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shouguouo
 * @date 2021-11-25 16:42:42
 */
@Component
@Data
public class Shou {

    @Value("shou")
    private String name;

    @Lookup
    public Xiang getXiang() {
        // spring will override this method using Cglib proxy
        return null;
    }
}
