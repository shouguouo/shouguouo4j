package com.shouguouo.spring.aop.full;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author shouguouo
 * @date 2021-11-28 00:23:11
 */
@Component
public class Do {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String doDoDo(String name, LocalDate date) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("name is empty");
        }
        if (date == null) {
            throw new RuntimeException("date is null");
        }
        return name + "'s birthday:" + date.format(FORMATTER);
    }
}
