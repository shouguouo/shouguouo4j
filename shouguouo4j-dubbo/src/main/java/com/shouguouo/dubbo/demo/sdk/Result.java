package com.shouguouo.dubbo.demo.sdk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shouguouo
 * @date 2022-05-26 16:03:38
 */
@Data
public class Result<T> implements Serializable {

    private T data;

    private Boolean success;

    private String msg;
}
