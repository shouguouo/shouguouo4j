package com.shouguouo.common.util;

import lombok.experimental.UtilityClass;

/**
 * @author shouguouo
 * @date 2021-11-28 12:56:03
 */
@UtilityClass
public class OutputUtils {

    public void cuttingLine(String message) {
        System.out.printf("-------------------%s-------------------%n", message);
    }
}
