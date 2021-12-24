package com.shouguouo.web.rocketmq.domain;

import lombok.Data;

/**
 * @author shouguouo
 * @date 2021-12-24 21:26:32
 */
@Data
public class UserIdModifier {

    private Long oldUserId;

    private Long newUserId;

    private Long version;
}
