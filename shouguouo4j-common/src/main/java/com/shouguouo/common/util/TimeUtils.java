package com.shouguouo.common.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author shouguouo
 * @date 2022-04-28 22:42:48
 */
@UtilityClass
public class TimeUtils {

    private final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault());
    }

    public String format(LocalDateTime time, DateTimeFormatter formatter) {
        return time.format(formatter);
    }

    public String format(long timestamp) {
        return format(toLocalDateTime(timestamp), TIME);
    }
}
