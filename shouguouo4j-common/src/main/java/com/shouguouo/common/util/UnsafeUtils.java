package com.shouguouo.common.util;

import lombok.experimental.UtilityClass;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author shouguouo
 * @date 2022-03-27 21:07:30
 */
@UtilityClass
public class UnsafeUtils {

    private static final Unsafe unsafe;

    static {
        Unsafe here;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            here = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            here = Unsafe.getUnsafe();
        }
        unsafe = here;
    }

    public static int pageSize() {
        return unsafe.pageSize();
    }
}
